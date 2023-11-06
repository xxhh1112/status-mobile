module.exports = {
  presets: ['module:metro-react-native-babel-preset'],
  plugins: ['react-native-reanimated/plugin', '@babel/plugin-transform-named-capturing-groups-regex','@babel/plugin-proposal-logical-assignment-operators'],
  env: {
    test: {
      presets: [
        '@babel/preset-react',
        [
          '@babel/preset-env',
          {
            targets: {
              node: '14',
            },
          },
        ],
      ],
    },
  },
};
