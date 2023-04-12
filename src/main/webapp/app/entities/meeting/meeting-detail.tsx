import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './meeting.reducer';

export const MeetingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const meetingEntity = useAppSelector(state => state.meeting.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="meetingDetailsHeading">Meeting</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{meetingEntity.id}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {meetingEntity.createdDate ? <TextFormat value={meetingEntity.createdDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="topic">Topic</span>
          </dt>
          <dd>{meetingEntity.topic}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{meetingEntity.description}</dd>
          <dt>
            <span id="meetingLink">Meeting Link</span>
          </dt>
          <dd>{meetingEntity.meetingLink}</dd>
          <dt>User</dt>
          <dd>{meetingEntity.user ? meetingEntity.user.id : ''}</dd>
          <dt>Group</dt>
          <dd>{meetingEntity.group ? meetingEntity.group.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/meeting" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/meeting/${meetingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MeetingDetail;
