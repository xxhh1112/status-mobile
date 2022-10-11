import * as React from 'react';
import {
    View,
    TouchableOpacity,
    Text,
    ScrollView,
    TextInput,
    Flatlist
} from 'react-native';
import { render, screen, fireEvent } from '@testing-library/react-native';

import "node-libs-react-native/globals";
// import y from "react-native-draggable-flatlist"
// import d from "react-native-draggable-flatlist"

// import x from "../../../../../app/index.js"
// import x from "../../../../../target/test/test.js"

// console.log(y)

// jest.mock('react-native-gesture-handler', () => jest.requireActual('react-native-gesture-handler'))
// jest.mock('react-native-draggable-flatlist', () => jest.requireActual("react-native-draggable-flatlist"))
import {checkbox} from "../../../../../target/unit-test/quo2.components.selectors.selectors"

// function QuestionsBoard({ questions, onSubmit }) {
//     const [data, setData] = React.useState({});

//     return (
//         <ScrollView>
//             {questions.map((q, index) => {
//                 return (
//                     <View key={q}>
//                         <Text>{q}</Text>
//                         <TextInput
//                             accessibilityLabel="answer input"
//                             accessibilityHint="input"
//                             onChangeText={(text) => {
//                                 setData((state) => ({
//                                     ...state,
//                                     [index + 1]: { q, a: text },
//                                 }));
//                             }}
//                         />
//                     </View>
//                 );
//             })}
//             <TouchableOpacity onPress={() => onSubmit(data)}>
//                 <Text>Submit</Text>
//             </TouchableOpacity>
//         </ScrollView>
//     );
// }

test('form submits two answers', () => {
    const allQuestions = ['q1', 'q2'];
    const mockFn = jest.fn();

    const { getAllByLabelText, getByText } = render(
        <Text>Submit</Text>
    );

    const answerInputs = getByText('Submit');

    // console.log(x)
    // expect(mockFn).toHaveBeenCalledWith({
    //     '1': { q: 'q1', a: 'a1' },
    //     '2': { q: 'q2', a: 'a2' },
    // });
});


