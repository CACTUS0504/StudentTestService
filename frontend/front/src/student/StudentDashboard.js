import React, { useEffect, useState } from 'react';
import logout from '../util/logout';
import { useLocalState } from '../util/useLocalState';
import Sidebar from './Sidebar';
import { Col, Container, Nav, Navbar, Row } from 'react-bootstrap';
import TestPage from './TestPage';

const StudentDashboard = () => {
    // Студенту доступен только просмотр тестов своих учителей
    // Нужно сделать, чтобы студент мог учиться на нескольких курсах (поменять связь на many-to-many)
    
    const [teachers, setTeachers] = useState(null);
    const [selectedTestId, setSelectedTestId] = useState(0);
    const [currentTestPage, setCurrentTestPage] = useState(1);
    const[jwt, setJwt] = useLocalState("", "jwt");
    // const [answers, setAnswers] = useState(() => {
    //     let storedValue = JSON.parse(localStorage.getItem('answers'));

    //     return storedValue !== null
    //         ? storedValue
    //         : '';
    // });

    // Добавить получение всех учителей
    useEffect(() => {
        fetch("edu/api/student/teachers", {
            headers: {
                "Authorization": `Bearer ${jwt}`,
                "student_id": 1 //Этот id проверять в gateway
            }
        }).then(response => {
            if (response.status === 200) {
                return response.json();
            }
        }).then(teachers => {
            setTeachers(teachers);
        })
    }, []);

    return (
        <>
        <Navbar bg="primary" variant="dark">
        <Container fluid className='justify-content-between'>
            <Row>
                <Col>
                    <Navbar.Brand href="#" className='ms-5'>TestSystem</Navbar.Brand>
                </Col>
            </Row>
            <Row>
                <Col>
                <Nav className="me-auto">
                    <Nav.Link href="#cabinet">Матвей Захаров</Nav.Link>
                    <Nav.Link onClick={() => logout()}>Выйти</Nav.Link>
                </Nav>
                </Col>
            </Row>
        </Container>
        </Navbar>
        <Container fluid>
            <Row>
                <Col lg='2'>
                    <Sidebar teachers={teachers} setSelectedTestId={setSelectedTestId}></Sidebar>
                </Col>
                <Col>
                    <TestPage selectedTestId={selectedTestId} currentTestPage={currentTestPage} setCurrentTestPage={setCurrentTestPage} studentId={1}></TestPage>
                </Col>
            </Row>
        </Container>
        </>
    );
};

export default StudentDashboard;