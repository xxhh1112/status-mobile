module.exports = {
    "preset": "react-native",
    "setupFilesAfterEnv": ["@testing-library/jest-native/extend-expect"
        , "./jestSetup.js"
    ],
    "setupFiles": [
    ],
    "testPathIgnorePatterns": [
        "timerUtils",
        "examples/"
    ],
    "testTimeout": 60000,
    "transformIgnorePatterns": [
        "/node_modules/(?!(@react-native|react-native|react-native-languages|react-native-shake|react-native-reanimated)/).*/"
    ],
    "globals": {
        "__DEV__": true
    },
    "testEnvironment": "node"
}