import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMeetingNote } from 'app/shared/model/meeting-note.model';
import { getEntities } from './meeting-note.reducer';

export const MeetingNote = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const meetingNoteList = useAppSelector(state => state.meetingNote.entities);
  const loading = useAppSelector(state => state.meetingNote.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="meeting-note-heading" data-cy="MeetingNoteHeading">
        Meeting Notes
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/meeting-note/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Meeting Note
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {meetingNoteList && meetingNoteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Detail</th>
                <th>User</th>
                <th>Meeting</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {meetingNoteList.map((meetingNote, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/meeting-note/${meetingNote.id}`} color="link" size="sm">
                      {meetingNote.id}
                    </Button>
                  </td>
                  <td>{meetingNote.detail}</td>
                  <td>{meetingNote.user ? meetingNote.user.id : ''}</td>
                  <td>{meetingNote.meeting ? <Link to={`/meeting/${meetingNote.meeting.id}`}>{meetingNote.meeting.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/meeting-note/${meetingNote.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/meeting-note/${meetingNote.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/meeting-note/${meetingNote.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Meeting Notes found</div>
        )}
      </div>
    </div>
  );
};

export default MeetingNote;
