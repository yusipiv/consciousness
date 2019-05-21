import React from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stomp-websocket';
import 'bootstrap/dist/css/bootstrap.css';
import './App.css';

class App extends React.Component {

    state = {prices: null};
    
    constructor(props) {
        super(props);
        this.URL = 'http://localhost:39099/gs-guide-websocket/';
        this.stompClient = null;
        this.socket = null;
        this.prices={};
    }

    setConnected (connected) {
        console.log(connected?'Socket Connected!': 'Socket Disconnected!');
    }

    connect() {
        this.socket = new SockJS(this.URL);
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.connect({}, frame => {
            this.setConnected(true);
            console.log('Connected: ' + frame);

            this.stompClient.subscribe('/topic/prices', greeting => {
                let rates = JSON.parse(greeting.body);
                console.log('should rates here:', rates);

                let instrument=`${rates.ccy}/${rates.base_ccy}`;


                this.prices[instrument] = {
                    'instrument': instrument,
                    'buy' : rates.buy,
                    'sale' : rates.sale
                };

                this.setState({prices:this.prices});

                console.log('-> ',this.state);
            });
        });
    }

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        this.setConnected(false);
    }

    componentDidMount() {
        this.connect();
    }

    componentWillUnmount() {
        this.disconnect();
    }

    render() {
        return <table className="table rates" >
            <thead>
            <tr>
                <th scope="col" >Instrument</th>
                <th scope="col" >Buy</th>
                <th scope="col" >Sale</th>
            </tr >
            </thead >
            <tbody>
                {
                    this.state.prices!=null?
                      Object.keys(this.state.prices).map((k) => {
                    return <tr>
                        <td >{this.state.prices[k].instrument}</td>
                        <td >{this.state.prices[k].buy}</td>
                        <td >{this.state.prices[k].sale}</td>
                    </tr>;
                    })
                      : "Loading..."
                }
            </tbody>
        </table >;
    }
}

export default App;