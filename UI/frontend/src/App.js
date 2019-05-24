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
    this.connected = false;
  }

  setConnected(connected) {
    this.connected = connected;
    console.log(connected ? 'Socket Connected!' : 'Socket Disconnected!');
  }

  connect() {
    this.stompClient = Stomp.over(new SockJS(this.URL));
    //this.stompClient.debug = null;
    this.stompClient.connect({}, this.onConnect, this.onDisconnect);
  }

  onConnect = () => {
    this.setConnected(true);
    this.stompClient.subscribe('/topic/prices', this.pricesSubscription());
    this.stompClient.subscribe('/topic/midValue', this.simulatorSubscription);
  };

  pricesSubscription = () => {
    let prices = {};
    return (rateValue) => {
      let rates = JSON.parse(rateValue.body);
      let instrument = `${rates.ccy}/${rates.base_ccy}`;
      prices[instrument] = {
        'instrument': instrument,
        'buy': rates.buy,
        'sale': rates.sale
      };
      this.setState({prices: prices});
    };
  };

  simulatorSubscription = (response) => {
    this.setState({midValue: JSON.parse(response.body)});
  };

  onDisconnect = (err) => {
    console.log(err);
    this.setConnected(false);
    setTimeout(this.connect, 5000);
  };

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