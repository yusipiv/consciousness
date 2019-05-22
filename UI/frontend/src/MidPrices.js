import React from 'react';
import './MidPrices.css';
import 'open-iconic/font/css/open-iconic-bootstrap.min.css';
import Arrow from './components/Arrow';

class MidPrices extends React.Component {

  constructor(props) {
    super(props);
    console.log('-->', props)
    //{"rate":7547.6914,"growthAbsolute":0.0000,"growthPercent":0}
  }

  render() {
    return <table className="table midPrices" >
      <thead >
      <tr >
        <th scope="col" >Instrument</th >
        <th scope="col" >Ask</th >
        <th scope="col" >Bid</th >
      </tr >
      </thead >
      <tbody >
      {
        this.props.prices != null ?
          Object.keys(this.props.prices).map((key) => {
            return <tr >
              <td >{key}</td >
              <td ><Arrow value={this.props.prices[key].ask}/></td >
              <td ><Arrow value={this.props.prices[key].bid}/></td >
            </tr >;
          })
          : "Loading..."
      }
      </tbody >
    </table >;
  }
}

export default MidPrices;