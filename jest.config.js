module.exports = {
    "preset": "react-native",
    "setupFilesAfterEnv": ["@testing-library/jest-native/extend-expect"
        , "../jestSetup.js"
    ],
    "setupFiles": [
    ],
    "testPathIgnorePatterns": [
        "timerUtils",
        "examples/"
    ],
    "testTimeout": 60000,
    "transformIgnorePatterns": [
        "/node_modules/(?!(@react-native|react-native-background-timer|react-native|rn-emoji-keyboard|react-native-languages|react-native-shake|react-native-reanimated)/).*/"
    ],
    "globals": {
        "__DEV__": true
    },
    "testEnvironment": "node",
    rootDir: "dist-test",
    testMatch: [
        "**/*_spec.js"
    ]
}

