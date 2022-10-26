import { useDerivedValue, interpolate } from 'react-native-reanimated';

// Generic Worklets

export function applyAnimationsToStyle(animations, style) {
  return function() {
    'worklet'
    
    var animatedStyle = {}
	
    for (var key in animations) {
      if (key == "transform") {
        var transforms = animations[key];
        var animatedTransforms = []
	
        for (var transform of transforms) {
          var transformKey = Object.keys(transform)[0];
          animatedTransforms.push({
            [transformKey]: transform[transformKey].value
          })
        }
	
        animatedStyle[key] = animatedTransforms;
      } else {
        animatedStyle[key] = animations[key].value;
      }
    }
    
    return Object.assign(animatedStyle, style);
  };
};

// Switcher Worklets

export function switcherCloseButtonOpacity (switcherButtonOpacity) {  
  return useDerivedValue(
    function () {
      'worklet'
      return 1 - switcherButtonOpacity.value;
    }
  );
}

export function switcherScreenRadius (switcherScreenSize) {
  return useDerivedValue(
    function () {
      'worklet'
      return switcherScreenSize.value/2;
    }
  );
}

export function switcherScreenBottomPosition (switcherScreenRadius, switcherPressedRadius, initalPosition) {
  return useDerivedValue(
    function () {
      'worklet'
      return initalPosition + switcherPressedRadius - switcherScreenRadius.value;
    }
  );
}

export function switcherContainerBottomPosition (switcherScreenBottom, heightOffset) {
  return useDerivedValue(
    function () {
      'worklet'
      return - (switcherScreenBottom.value + heightOffset);
    }
  );
}

export function interpolateValue(sharedValue, inputRange, outputRange) {
  return useDerivedValue(
    function () {
      'worklet'
      return interpolate(sharedValue.value, inputRange, outputRange);
    }
  );
}

const MAX_SCALE = 1.8;

export function ringScale(scale, substract) {
  return useDerivedValue(
    function () {
      'worklet'
      const value = scale.value;
      const maxDelta = MAX_SCALE - 1;
      const maxDeltaDiff = 1 - maxDelta;
      const maxVirtualScale = MAX_SCALE + maxDelta;
      const decimals = value - Math.floor(value);
      const normalizedValue = value >= maxVirtualScale ? (decimals + ((parseInt(value) - 1) * maxDeltaDiff) + 1) : value;
      return (((normalizedValue - substract) > MAX_SCALE ? normalizedValue - maxDelta : normalizedValue) - substract);
    }
  );
}
