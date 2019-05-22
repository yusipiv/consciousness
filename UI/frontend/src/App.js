import React from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stomp-websocket';
import 'bootstrap/dist/css/bootstrap.css';
import './App.css';
import Rates from "./Rates";
import MidPrices from "./MidPrices";

class App extends React.Component {

  state = {prices: null, midValue: null};

  constructor(props) {
    super(props);
    this.URL = 'http://localhost:39099/gs-guide-websocket/';
    this.stompClient = null;
    this.socket = null;
    this.prices = {};
    this.midValue = {};
  }

  setConnected(connected) {
    console.log(connected ? 'Socket Connected!' : 'Socket Disconnected!');
  }

  connect() {
    this.socket = new SockJS(this.URL);
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect({}, frame => {
      this.setConnected(true);
      console.log('Connected: ' + frame);

      this.stompClient.subscribe('/topic/prices', rateValue => {
        let rates = JSON.parse(rateValue.body);
        let instrument = `${rates.ccy}/${rates.base_ccy}`;
        this.prices[instrument] = {
          'instrument': instrument,
          'buy': rates.buy,
          'sale': rates.sale
        };
        this.setState({prices: this.prices});
      });

      this.stompClient.subscribe('/topic/midValue', response => {
        this.midValue = JSON.parse(response.body);
          this.setState({midValue: this.midValue});
        console.log(this.state);
        }
      );
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
    return <div >
      <Rates prices={this.state.prices} />
      <MidPrices prices={this.state.midValue} />
    </div >;
  }
}

export default App;