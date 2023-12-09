import React, { useEffect, useState } from 'react';
import { Accordion, Button, Card } from 'react-bootstrap';
import { useAccordionButton } from 'react-bootstrap/AccordionButton';
import { useLocalState } from '../util/useLocalState';

function CustomToggle({ children, eventKey, setEventKey }) {

    const decoratedOnClick = useAccordionButton(eventKey, () =>{
    }
    );
  
    return (
      <Button variant="outline-primary" type="button" style={{width:"100%"}} onClick={decoratedOnClick}>
        {children}
      </Button>
    );
  }

// Функция для получения разметки со всеми тестами учителя
const SidebarTeacherButton = (props) => {
    const [tests, setTests] = useState(null);
    const[jwt, setJwt] = useLocalState("", "jwt");

    useEffect(() => {
        fetch("edu/api/teacher/tests", {
            headers: {
                "Authorization": `Bearer ${jwt}`,
                "teacher_id": props.teacher.id
            }
        }).then(response => {
            if (response.status === 200) {
                return response.json();
            }
        }).then(tests => {
            setTests(tests);
        })
    }, []);

    return (
        <Card>
            <Card.Header>
                <CustomToggle eventKey={props.teacher.id} setEventKey={props.setEventKey}>{props.teacher.surname + " " + props.teacher.firstName + " " + props.teacher.middle_name}</CustomToggle>
            </Card.Header>
            <Accordion.Collapse eventKey={props.teacher.id}>
                <Card.Body>
                    {tests ? tests.map((test) => (
                            <Button onClick={() => {props.setSelectedTestId(test.id)}} style={{width:"100%"}} className='d-flex flex-column flex-md-row justify-content-center mb-3'>{test.title}</Button>)
                    ) : <></>}
                </Card.Body>
            </Accordion.Collapse>
        </Card>
    );
};

export default SidebarTeacherButton;