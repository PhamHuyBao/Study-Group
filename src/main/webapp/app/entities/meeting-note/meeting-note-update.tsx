import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IMeeting } from 'app/shared/model/meeting.model';
import { getEntities as getMeetings } from 'app/entities/meeting/meeting.reducer';
import { IMeetingNote } from 'app/shared/model/meeting-note.model';
import { getEntity, updateEntity, createEntity, reset } from './meeting-note.reducer';

export const MeetingNoteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const meetings = useAppSelector(state => state.meeting.entities);
  const meetingNoteEntity = useAppSelector(state => state.meetingNote.entity);
  const loading = useAppSelector(state => state.meetingNote.loading);
  const updating = useAppSelector(state => state.meetingNote.updating);
  const updateSuccess = useAppSelector(state => state.meetingNote.updateSuccess);

  const handleClose = () => {
    navigate('/meeting-note');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getMeetings({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...meetingNoteEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      meeting: meetings.find(it => it.id.toString() === values.meeting.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...meetingNoteEntity,
          user: meetingNoteEntity?.user?.id,
          meeting: meetingNoteEntity?.meeting?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studyGroupApp.meetingNote.home.createOrEditLabel" data-cy="MeetingNoteCreateUpdateHeading">
            Create or edit a Meeting Note
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="meeting-note-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Detail" id="meeting-note-detail" name="detail" data-cy="detail" type="text" />
              <ValidatedField id="meeting-note-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="meeting-note-meeting" name="meeting" data-cy="meeting" label="Meeting" type="select">
                <option value="" key="0" />
                {meetings
                  ? meetings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/meeting-note" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MeetingNoteUpdate;
