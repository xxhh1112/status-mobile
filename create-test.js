
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
    const names = ["preview-button"] // revert to fileNames
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

// [] =>
// [{},{}]

// {
//     "label": "Size:",
//     "key": "size",
//     "type": "select",
//     "options": [
//         {
//             "key": "heading-1",
//             "value": "Heading 1"
//         },
//         {
//             "key": "heading-2",
//             "value": "Heading 2"
//         },
//     ]
// },
// => 
// [{
//     "size":{
//     "type": "select",
//     "value": "Heading 1"
//     },
//     {
//         "size":{
//         "type": "select",
//         "value": "Heading 2"
//         },
//     ]

const getFlattenedVariants = (variant) => {
    if (variant['type'] == "select") {
        return variant['options'].map(({ value }) => ({
            [variant['key']]: {
                "type": "select",
                "value": value
            }
        }))
    }
    if (variant['type'] == "boolean") {
        return [
            {
                [variant['key']]: {
                    "type": "boolean",
                    "value": "True"
                }
            },
            {
                [variant['key']]: {
                    "type": "boolean",
                    "value": "False"
                }
            },
        ]
    }
    if (variant['type'] == "text") {
        return [
            {
                [variant['key']]: {
                    "type": "boolean",
                    "value": "Test Data"
                }
            },
        ]
    }

    return []
}


// [
//     { size: { type: 'select', value: 'Heading 1' } },
//     { size: { type: 'select', value: 'Heading 2' } },
//     { size: { type: 'select', value: 'Paragraph 1' } },
//     { size: { type: 'select', value: 'Paragraph 2' } },
//     { size: { type: 'select', value: 'Label' } },

// { weight: { type: 'select', value: 'Regular' } },
// { weight: { type: 'select', value: 'Medium' } },
// { weight: { type: 'select', value: 'Semi-bold' } },
// { weight: { type: 'select', value: 'Monospace' } }
//   ]

// variants - 2
//  [
//   [
//     { type: { type: 'select', value: 'Ghost' } },
//     { type: { type: 'select', value: 'Danger' } }
//   ],
//   [
//     { size: { type: 'select', value: '32' } },
//     { size: { type: 'select', value: '24' } }
//   ],
//   [
//     { icon: { type: 'boolean', value: 'True' } },
//     { icon: { type: 'boolean', value: 'False' } }
//    ]
//  ]

//       [
//     { type: { type: 'select', value: 'Ghost' } },
//     { type: { type: 'select', value: 'Danger' } }
//   ],
//   [
//     { size: { type: 'select', value: '32' } },
//     { size: { type: 'select', value: '24' } }
//   ],

// [
//     { size: { type: 'select', value: '32' } },
//     { size: { type: 'select', value: '24' } }
//   ]
// 
// [
//     [
//         {
//             "type": {
//                 "type": "select",
//                 "value": "Primary"
//             }
//         },
//         {
//             "size": {
//                 "type": "select",
//                 "value": "32"
//             }
//         }
//     ],

// ]


const getPermutations = (allOptions) => Object.entries(allOptions).reduce((acc, [fileName, options]) => {
    const variants = options.map((option) => getFlattenedVariants(option))
    const initialVariant = variants[0].map((v) => [v])
    const o = variants.slice(1,).reduce((acc, cur) => {


        return acc.flatMap(a => cur.map(c => [...a, c]))
    }, initialVariant)


    console.log(o.length)
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