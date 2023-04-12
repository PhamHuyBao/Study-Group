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
import { IGroup } from 'app/shared/model/group.model';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { IMeetingNote } from 'app/shared/model/meeting-note.model';
import { getEntities as getMeetingNotes } from 'app/entities/meeting-note/meeting-note.reducer';
import { IMeeting } from 'app/shared/model/meeting.model';
import { getEntity, updateEntity, createEntity, reset } from './meeting.reducer';

export const MeetingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const groups = useAppSelector(state => state.group.entities);
  const meetingNotes = useAppSelector(state => state.meetingNote.entities);
  const meetingEntity = useAppSelector(state => state.meeting.entity);
  const loading = useAppSelector(state => state.meeting.loading);
  const updating = useAppSelector(state => state.meeting.updating);
  const updateSuccess = useAppSelector(state => state.meeting.updateSuccess);

  const handleClose = () => {
    navigate('/meeting');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getGroups({}));
    dispatch(getMeetingNotes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...meetingEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      group: groups.find(it => it.id.toString() === values.group.toString()),
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
          ...meetingEntity,
          user: meetingEntity?.user?.id,
          group: meetingEntity?.group?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studyGroupApp.meeting.home.createOrEditLabel" data-cy="MeetingCreateUpdateHeading">
            Create or edit a Meeting
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="meeting-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField label="Created Date" id="meeting-createdDate" name="createdDate" data-cy="createdDate" type="date" />
              <ValidatedField label="Topic" id="meeting-topic" name="topic" data-cy="topic" type="text" />
              <ValidatedField label="Description" id="meeting-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Meeting Link" id="meeting-meetingLink" name="meetingLink" data-cy="meetingLink" type="text" />
              <ValidatedField id="meeting-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="meeting-group" name="group" data-cy="group" label="Group" type="select">
                <option value="" key="0" />
                {groups
                  ? groups.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/meeting" replace color="info">
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

export default MeetingUpdate;
