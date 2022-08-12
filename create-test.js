
const testData = require('./visual-test-config.json')
const fs = require('fs-extra')

const data = testData["analysis"]

const screenNames = data["var-usages"].filter((
    usage => usage["from-var"] && usage["from-var"] == "screens"
)).map(({ name }) => name)


fs.writeFileSync("e2e/visual-tests.json", JSON.stringify(screenNames))
