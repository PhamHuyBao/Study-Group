import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './meeting-note.reducer';

export const MeetingNoteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const meetingNoteEntity = useAppSelector(state => state.meetingNote.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="meetingNoteDetailsHeading">Entity translation missing for studyGroupApp.meetingNote.detail.title</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{meetingNoteEntity.id}</dd>
          <dt>
            <span id="detail">Detail</span>
          </dt>
          <dd>{meetingNoteEntity.detail}</dd>
          <dt>User</dt>
          <dd>{meetingNoteEntity.user ? meetingNoteEntity.user.id : ''}</dd>
          <dt>Meeting</dt>
          <dd>{meetingNoteEntity.meeting ? meetingNoteEntity.meeting.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/meeting-note" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/meeting-note/${meetingNoteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MeetingNoteDetail;
