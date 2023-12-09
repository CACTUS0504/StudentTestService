import React from 'react';
import { Accordion, Button, Col, Container, Navbar, Row } from 'react-bootstrap';
import SidebarTeacherButton from './SidevarTeacherButton';

const Sidebar = (props) => {
    return (
        <Container fluid>
            <Row>
                <Col className='bg-light min-vh-100'>
                    <Navbar.Brand className='fs-5'>Мои преподаватели</Navbar.Brand>
                    <Accordion>
                        {props.teachers ? props.teachers.map((teacher) => (
                            <SidebarTeacherButton teacher={teacher} setSelectedTestId={props.setSelectedTestId}></SidebarTeacherButton>)
                        ) : <></>}
                    </Accordion>
                </Col>
            </Row>
        </Container>
    );
};

export default Sidebar;