import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMeeting } from 'app/shared/model/meeting.model';
import { getEntities } from './meeting.reducer';

export const Meeting = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const meetingList = useAppSelector(state => state.meeting.entities);
  const loading = useAppSelector(state => state.meeting.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="meeting-heading" data-cy="MeetingHeading">
        Meetings
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/meeting/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Meeting
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {meetingList && meetingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Id</th>
                <th>Created Date</th>
                <th>Topic</th>
                <th>Description</th>
                <th>Meeting Link</th>
                <th>User</th>
                <th>Group</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {meetingList.map((meeting, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/meeting/${meeting.id}`} color="link" size="sm">
                      {meeting.id}
                    </Button>
                  </td>
                  <td>
                    {meeting.createdDate ? <TextFormat type="date" value={meeting.createdDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{meeting.topic}</td>
                  <td>{meeting.description}</td>
                  <td>{meeting.meetingLink}</td>
                  <td>{meeting.user ? meeting.user.id : ''}</td>
                  <td>{meeting.group ? <Link to={`/group/${meeting.group.id}`}>{meeting.group.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/meeting/${meeting.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/meeting/${meeting.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/meeting/${meeting.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Meetings found</div>
        )}
      </div>
    </div>
  );
};

export default Meeting;
