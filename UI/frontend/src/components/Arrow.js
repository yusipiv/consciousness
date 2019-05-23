import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import './Arrow.css';

class Arrow extends React.Component {

  constructor(props) {
    super(props);
    this.formatter = new Intl.NumberFormat('en-US', {
      style: 'decimal',
      minimumFractionDigits: 4,
      maximumFractionDigits: 4
    });
    this.percentFormatter = new Intl.NumberFormat('en-US', {
      style: 'percent',
      minimumFractionDigits: 4,
      maximumFractionDigits: 4
    });

  }

  render() {

    return <span>
        <span> {this.formatter.format(this.props.value.rate)} </span>
        {this.props.value.growthAbsolute !== 0 ?
            <span className={this.props.value.growthAbsolute > 0 ? 'oi oi-arrow-top' : 'oi oi-arrow-bottom'}>
              <sup>{this.percentFormatter.format(this.props.value.growthPercent > 0? '+' + this.props.value.growthPercent : this.props.value.growthPercent)}</sup>
              <sub>{this.formatter.format(this.props.value.growthAbsolute > 0? '+' + this.props.value.growthAbsolute : this.props.value.growthAbsolute)}</sub>
            </span>
          : ''
        }
        </span>;
  }
}

export default Arrow;