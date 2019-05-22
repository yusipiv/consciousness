import React from 'react';
import './Rates.css';

class Rates extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return <table className="table rates" >
      <thead >
      <tr >
        <th scope="col" >Instrument</th >
        <th scope="col" >Buy</th >
        <th scope="col" >Sale</th >
      </tr >
      </thead >
      <tbody >
      {
        this.props.prices != null ?
          Object.keys(this.props.prices).map((k) => {
            return <tr >
              <td >{this.props.prices[k].instrument}</td >
              <td >{this.props.prices[k].buy}</td >
              <td >{this.props.prices[k].sale}</td >
            </tr >;
          })
          : "Loading..."
      }
      </tbody >
    </table >;
  }
}

export default Rates;