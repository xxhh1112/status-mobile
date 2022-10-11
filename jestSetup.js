// require('./test-resources/override.js')
const WebSocket = require('ws');
const { NativeModules } = require('react-native');
const mockAsyncStorage = require('@react-native-async-storage/async-storage/jest/async-storage-mock');

require('react-native-gesture-handler/jestSetup');

jest.mock('react-native-reanimated', () => {
    const Reanimated = require('react-native-reanimated/mock');

    // The mock for `call` immediately calls the callback which is incorrect
    // So we override it with a no-op
    Reanimated.default.call = () => { };

    return Reanimated;
});

// Silence the warning: Animated: `useNativeDriver` is not supported because the native animated module is missing
// jest.mock('react-native/Libraries/Animated/NativeAnimatedHelper');

jest.mock('@react-native-async-storage/async-storage', () => mockAsyncStorage);

jest.mock('react-native-navigation', () => ({
    getNavigationConstants:
        () => ({ constants: [] }),
    Navigation: { constants: async () => { } }
}));


jest.mock('react-native-languages', () => ({
    RNLanguages: {
        language: 'en',
        languages: ['en'],
    },
    default: {}
}));




NativeModules.ReactLocalization = {
    language: 'en',
};
global.navigator = {
    userAgent: 'node',
}

global.WebSocket = WebSocket



