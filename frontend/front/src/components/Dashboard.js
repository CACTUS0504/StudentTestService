import React from 'react';
import { useLocalState } from '../util/useLocalState';

const Dashboard = (props) => {
    const [jwt, setJwt] = useLocalState("", "jwt");
    return (
        <div>
            <h1>JWT component</h1>
            <div>JWT value is {jwt}</div>
        </div>
    );
};

export default Dashboard;