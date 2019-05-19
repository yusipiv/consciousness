import React from 'react';

class ProductDetail extends React.Component {

    constructor(props) {
        super(props);
        this.URL = 'ws://localhost:41905/myHandler' ;
        this.ws = new WebSocket(this.URL);

        this.ws.onopen = () => {
            // on connecting, do nothing but log it to the console
            console.log('connected')
        };

        this.ws.onmessage = evt => {
            // on receiving a message, add it to the list of messages
            console.log(evt.data);
        };

        this.ws.onclose = () => {
            console.log('disconnected');
            // automatically try to reconnect on connection loss
            this.setState({
                ws: new WebSocket(this.URL),
            });
        };
    }

    componentDidMount() {
        console.log('did mount');

    }

    render() {
        return (
            <div>
                <label htmlFor="name">
                    Name:&nbsp;

                </label>
            </div>
        );
    }
}

export default ProductDetail;