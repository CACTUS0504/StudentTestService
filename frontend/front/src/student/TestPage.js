import React, { useEffect, useRef, useState } from 'react';
import { useLocalState } from '../util/useLocalState';
import { Pagination, Button, Form } from 'react-bootstrap';
import io from 'socket.io-client';

const TestPage = (props) => {
    const [test, setTest] = useState(null);
    const [pages, setPages] = useState([]);
    const [answers, setAnswers] = useState([]);
    const[jwt, setJwt] = useLocalState("", "jwt");

    const firstRender = useRef(true);

    function startTest() {
        return fetch("edu/api/test/start", {
            method: 'POST',
            headers:{
                "test_id": props.selectedTestId,
                "student_id": props.studentId,
                "Authorization": `Bearer ${jwt}`,
                "Content-Type": "application/json"
            },
        })
    }

    function addAnswer(answer) {
        return fetch("edu/api/test/answer?question_id=" + props.currentTestPage, {
            method: 'POST',
            headers:{
                "test_id": props.selectedTestId,
                "answer": answer,
                "student_id": props.studentId,
                "Authorization": `Bearer ${jwt}`,
                "Content-Type": "application/json"
            },
        })
    }

    function getAnswers() {
        return fetch("edu/api/test/student-answers", {
            headers:{
                "test_id": props.selectedTestId,
                "student_id": props.studentId,
                "Authorization": `Bearer ${jwt}`,
                "Content-Type": "application/json"
            },
        }).then(response => response.json())
    }

    useEffect(() => {
        

    }, [answers])


    useEffect(() => {
        if (props.selectedTestId === 0) return;
        fetch("edu/api/test",{
            headers:{
                "test_id": props.selectedTestId,
                "Authorization": `Bearer ${jwt}`,
                "Content-Type": "application/json"
            },
        }).then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                return Promise.reject("Can't find test with id=" + props.selectedTestId);
            }
        }).then(test => {
            startTest().then(() => {
                getAnswers().then(answers => {
                    let arr = [];
                    for (let i = 0; i < test.questions.length; i++) {
                        arr.push(0);
                    }
                    answers.forEach(result => {
                        arr[result.questionNum - 1] = result.answer;
                    });
                    setAnswers(arr);
                }).then(() =>{
                    props.setCurrentTestPage(1);
                    let newPages=[];
                    for (let i = 1; i <= test.questions.length; i++) {
                        newPages.push(
                            <div style={{marginLeft:'5px'}}>
                                <Pagination.Item active={answers && answers[i - 1] !== 0} key={i} onClick={() => props.setCurrentTestPage(i)}>
                                    {i}
                                </Pagination.Item>
                            </div>
                        );
                    }
                    setPages(newPages);
                    setTest(test);
                })
            }).catch((message) => {
                console.log(message);
            });
            })

    }, [props.selectedTestId])

    // Код дублируется, подумать над рефакторингом
    useEffect(() => {
        let newPages=[];
        for (let i = 1; i <= pages.length; i++) {
            console.log(answers);
            newPages.push(
                <div style={{marginLeft:'5px'}}>
                    <Pagination.Item active={answers && answers[i - 1] !== 0} key={i} onClick={() => props.setCurrentTestPage(i)}>
                        {i}
                    </Pagination.Item>
                </div>
            );
        }
        setPages(newPages);
    }, [answers])

    function nextPage(){
        if (props.currentTestPage === test.questions.length) return;
        props.setCurrentTestPage(props.currentTestPage + 1);
    }

    function prevPage(){
        if (props.currentTestPage === 1) return;
        props.setCurrentTestPage(props.currentTestPage - 1);
    }

    function updateAnswers(e) {
        addAnswer(e.target.value).then(() => {
            getAnswers().then(answers => {
                let arr = [];
                for (let i = 0; i < test.questions.length; i++) {
                    arr.push(0);
                }
                answers.forEach(result => {
                    arr[result.questionNum - 1] = result.answer;
                });
                setAnswers(arr);
            });
        })
    }

    function sendAnswers(){
        fetch("edu/api/test/end", {
            method: 'POST',
            headers:{
                "test_id": props.selectedTestId,
                "student_id": props.studentId,
                "Authorization": `Bearer ${jwt}`,
                "Content-Type": "application/json"
            },
        }).then(response => response.json())
        .then(res => console.log(res));
    }


    return (
        <>
        {test && test.questions.length !== 0?(
            <>
            <div>
                {test.questions[props.currentTestPage - 1].content}
            </div>
            <Form>
                <Form.Check checked={answers[props.currentTestPage - 1] === 1} onChange={updateAnswers} name="answers" type={'radio'} label={`a. ${test.questions[props.currentTestPage - 1].option_1}`} id='1' value={1}/>
                <Form.Check checked={answers[props.currentTestPage - 1] === 2} onChange={updateAnswers} name="answers" type={'radio'} label={`b. ${test.questions[props.currentTestPage - 1].option_2}`} id='2' value={2}/>
                <Form.Check checked={answers[props.currentTestPage - 1] === 3} onChange={updateAnswers} name="answers" type={'radio'} label={`c. ${test.questions[props.currentTestPage - 1].option_3}`} id='3' value={3}/>
                <Form.Check checked={answers[props.currentTestPage - 1] === 4} onChange={updateAnswers} name="answers" type={'radio'} label={`d. ${test.questions[props.currentTestPage - 1].option_4}`} id='4' value={4}/>
            </Form>
            <Pagination style={{marginTop:'20px'}}>
                {pages}
            </Pagination>
            <Button style={{marginRight:'10px'}} className='Primary' onClick={() => prevPage()}>Предыдущий вопрос</Button>
            <Button className='Primary' onClick={() => nextPage()}>Следующий вопрос</Button>
            <br />
            <Button variant='secondary' style={{marginTop:'50px'}} onClick={() => sendAnswers()}>Завершить тест</Button>
            </>
            ) : (<></>)}
        </>
    );
};

export default TestPage;