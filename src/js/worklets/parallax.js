import {
    useAnimatedStyle,
    useAnimatedSensor,
    SensorType,
    withTiming,
    interpolate
} from 'react-native-reanimated';

const OFFSET = 30;
const PI = Math.PI;
const HALF_PI = PI / 2;

export function sensorAnimatedImage(sensor, order) {
    return useAnimatedStyle(function () {
        'worklet'
        const { pitch, roll } = sensor.sensor.value;
console.log(pitch, roll, "dsadas")
        const top = withTiming(
            interpolate(
            pitch,
            Platform.OS === 'ios' ? [-HALF_PI, HALF_PI] : [HALF_PI, -HALF_PI],
            [-OFFSET / order - OFFSET, OFFSET / order - OFFSET]
            ),
            { duration: 100 }
        );
        const left = withTiming(
            interpolate(
            roll,
            [-PI, PI],
            [(-OFFSET * 2) / order - OFFSET, (OFFSET * 2) / order - OFFSET]
            ),
            { duration: 100 }
        );
        const right = withTiming(
            interpolate(
            roll,
            [-PI, PI],
            [(OFFSET * 2) / order - OFFSET, (-OFFSET * 2) / order - OFFSET]
            ),
            { duration: 100 }
        );
        const bottom = withTiming(
            interpolate(
            pitch,
            Platform.OS === 'ios' ? [-HALF_PI, HALF_PI] : [HALF_PI, -HALF_PI],
            [OFFSET / order - OFFSET, -OFFSET / order - OFFSET]
            ),
            { duration: 10 }
        );
        return {
            top,
            left,
            right,
            bottom,
        }
    })
}
