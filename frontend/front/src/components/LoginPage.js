import React, { useState } from 'react';
import { useLocalState } from '../util/useLocalState';
import { Button, Col, Container, Form, Row } from 'react-bootstrap';

const LoginPage = () => {
    const[username, setUsername] = useState("");
    const[password, setPassword] = useState("");

    const[jwt, setJwt] = useLocalState("", "jwt");

    function sendLoginRequest() {
        const user = {
        "login" : username,
        "password" : password
        }

        fetch("auth/login",{
        headers:{
            "Content-Type": "application/json"
        },
        method: "post",
        body: JSON.stringify(user)

        }).then((response) => {
            if (response.status === 200) {
                return Promise.all([response.json()]);
            } else {
                return Promise.reject("invalid login/password");
            }
        })
        .then(([body]) => {
            setJwt(body.token);
            if (body.roles.includes('TEACHER')) {
                window.location.href = "/teacher"
            } else if (body.roles.includes('STUDENT')) {
                window.location.href = "/student"
            }
        }).catch((message) => {
            alert(message);
        });
    }

    return (
        <Container className='mt-5'>
        <Row className='justify-content-center'>
            <Col md='8' lg='6'>
                <Form.Group className="mb-3">
                    <Form.Label className='fs-4'>Имя пользователя</Form.Label> 
                    <Form.Control type="text" id = "username" placeholder='Введите логин' value={username} onChange={(event) => {setUsername(event.target.value)}}></Form.Control> 
                </Form.Group>
            </Col>
        </Row>
        <Row className='justify-content-center'>
            <Col md='8' lg='6'>
                <Form.Group className="mb-3">
                    <Form.Label className='fs-4'>Пароль</Form.Label>
                    <Form.Control type="password" id = "password" placeholder='Введите пароль' value={password} onChange={(event) => {setPassword(event.target.value)}}></Form.Control>
                </Form.Group>
            </Col>
        </Row>
        <Row className='justify-content-center'>
            <Col md='8' lg='6' className='d-flex flex-column flex-md-row'>
                    <Button id="loginButton" type='button' onClick={() => sendLoginRequest()}> Отправить </Button>
            </Col>
        </Row>
        </Container>
    );
};

export default LoginPage;