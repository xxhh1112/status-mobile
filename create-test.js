
const testData = require('./visual-test-config.json')
const fs = require('fs-extra')
const { parseEDNString } = require('edn-data')
const { createReadStream } = require('fs');

const data = testData["analysis"]

const screenNames = data["var-usages"].filter((
    usage => usage["from-var"] && usage["from-var"] == "screens" && usage["from"] == "quo2.screens.main"
)).map(({ name }) => name)

const getOptions = async (fileNames) => {
    const acc = {}
    const names = ["preview-tabs","preview-segmented","preview-counter"] // revert to fileNames
    for await (const fileName of names) {
        const value = new Promise((res) => {
            const fileNameWithoutPreview = fileName.substring(8)
            const option = createReadStream(`./src/quo2/screens/${fileNameWithoutPreview}-options.edn`)
            option.on("data", function (chunk) {
                option.destroy();
                return res(parseEDNString(chunk,
                    { mapAs: 'object', keywordAs: 'string' }))
            })
        })
        acc[fileName] = await value
    }
    return acc
}

const getFlattenedVariants = (variant) => {
    if (variant['type'] == "select") {
        return variant['options'].map(({ value }) => ({
            "key": variant['key'],
            "type": "select",
            "value": value,
            label: variant['label']
        }))
    }
    if (variant['type'] == "boolean") {
        return [
            {
                "key": variant['key'],
                "type": "boolean",
                "value": "true",
                label: variant['label']
            },
            {
                "key": variant['key'],
                "type": "boolean",
                "value": "false",
                label: variant['label']
            },
        ]
    }
    if (variant['type'] == "text") {
        return [
            {
                "key": variant['key'],
                "type": "text",
                "value": "Test Data",
                label: variant['label']
            },
            {
                "key": variant['key'],
                "type": "text",
                "value": "123",
                label: variant['label']
            },
        ]
    }

    return []
}

const getPermutations = (allOptions) => Object.entries(allOptions).reduce((acc, [fileName, options]) => {
    const variants = options.map((option) => getFlattenedVariants(option))
    const initialVariant = variants[0].map((v) => [v])
    const o = variants.slice(1,).reduce((acc, cur) => {


        return acc.flatMap(a => cur.map(c => [...a, c]))
    }, initialVariant)
    return {
        [fileName]: o,
        ...acc
    }
}, {})

const runFile = async () => {
    const options = await getOptions(screenNames)
    const permutations = getPermutations(options)
    fs.writeFileSync("e2e/visual-tests.json", JSON.stringify(permutations))
}

runFile()