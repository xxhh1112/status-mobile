import {
    useDerivedValue,
    useAnimatedStyle,
    useAnimatedSensor,
    SensorType,
    withTiming,
    interpolate
} from 'react-native-reanimated';

const IMAGE_OFFSET = 100;
const PI = Math.PI;
const HALF_PI = PI / 2;

// export function useAnimatedStyleWorklet(...args) {
//     'worklet'
//     return useAnimatedStyle(...args)
// }

export function useAnimatedSensorWorklet() {
    return useDerivedValue(
        function () {
            'worklet'
           return useAnimatedSensor( SensorType.ROTATION)
        })
}

export function sensorAnimatedImage({ pitch, roll }) {
    return useDerivedValue(
        function () {
            'worklet'
            const order = 1
            return {
                top: withTiming(
                    interpolate(
                        pitch,
                        [-HALF_PI, HALF_PI],
                        [(-IMAGE_OFFSET * 2) / order, 0]
                    ),
                    { duration: 100 }
                ),
                left: withTiming(
                    interpolate(roll, [-PI, PI], [(-IMAGE_OFFSET * 2) / order, 0]),
                    {
                        duration: 100,
                    }
                ),
            }

        })
}
