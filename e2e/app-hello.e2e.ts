// @ts-nocheck
const data = require('./visual-tests.json')
const waitToNavigate = duration => new Promise(resolve => setTimeout(() => resolve(), duration));


describe('Example (hello)', () => {
    beforeAll(async () => {
        if (device.getPlatform() === 'ios') {
            await device.setStatusBar({ time: '12:34', dataNetwork: 'wifi', wifiBars: '3', batteryState: 'charging', batteryLevel: '100' });
        }
        await device.reloadReactNative();

        await element(by.text('I accept Status Terms of Use')).tap();
        // close alert
        await element(by.text('Get started')).tap();
        await waitToNavigate(800);

        await element(by.text('Generate keys')).tap();
        await waitToNavigate(800);

        await element(by.text('Next')).tap();
        await waitToNavigate(800);
        await element(by.text('Next')).tap();
        await waitToNavigate(800);
        // await element(by.text('Password...')).typeText('infinitbility@gmail.com');
        // await element(by.text('Confirm your password...')).typeText('infinitbility@gmail.com');
        await element(by.text('Next')).tap();
        await waitToNavigate(800);
        await element(by.text('Maybe later')).tap();
        await waitToNavigate(800);
        await element(by.text("Let's go")).tap();
        await waitToNavigate(800);

        await element(by.text("1")).tap();
        await waitToNavigate(800);
        await element(by.text("Quo2.0 Preview")).tap();
        await waitToNavigate(800);
    })
    beforeEach(async () => {
    });
    afterEach(async () => {
        await element(by.text("1")).tap();
        await waitToNavigate(800);
        await element(by.text("Quo2.0 Preview")).tap();
        await waitToNavigate(800);
    });


    for (item of data) {
        it(`${item} page should match snapshot`, async () => {
            await element(by.text(`Preview : quo2 - ${item}`)).tap();
            await waitToNavigate(800);
            await jestExpect(item).toMatchImageSnapshot();
        })
    }



});