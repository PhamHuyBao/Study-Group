import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>
          Copyright © 2023 Group Study Team | <a href="terms.html">Terms of Use</a>| <a href="privacy.html">Privacy Policy</a>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
