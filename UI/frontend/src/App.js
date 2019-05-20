import React from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stomp-websocket';
import ReactTable from 'react-table'
import 'react-table/react-table.css'

class App extends React.Component {

    constructor(props) {
        super(props);
        this.URL = 'http://localhost:39099/gs-guide-websocket/';
        this.stompClient = null;
        this.socket = null;

        this.data = {};
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
                this.data[instrument] = {
                    'instrument': instrument,
                    'buy' : rates.buy,
                    'sale' : rates.sale
                };
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
        const columns = [{
            Header: 'Instrument',
            accessor: 'instrument'
        }, {
            Header: 'Buy',
            accessor: 'buy'

        }, {
            Header: 'Sale',
            accessor: 'sale'
        }];
        return <ReactTable
          data={this.data}
          columns={columns}
          resolveData={data=>Object.keys(data).map((k) => data[k])}
        />
    }
}

export default App;