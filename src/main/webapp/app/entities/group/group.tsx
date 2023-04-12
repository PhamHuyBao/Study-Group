import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGroup } from 'app/shared/model/group.model';
import { getEntities } from './group.reducer';

export const Group = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const groupList = useAppSelector(state => state.group.entities);
  const loading = useAppSelector(state => state.group.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="group-heading" data-cy="GroupHeading">
        Groups
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/group/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Group
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {groupList && groupList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Groupname</th>
                <th>Group Owner Id</th>
                <th>Category</th>
                <th>Status</th>
                <th>Description</th>
                <th>Created Date</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {groupList.map((group, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/group/${group.id}`} color="link" size="sm">
                      {group.id}
                    </Button>
                  </td>
                  <td>{group.groupname}</td>
                  <td>{group.groupOwnerId}</td>
                  <td>{group.category}</td>
                  <td>{group.status ? 'true' : 'false'}</td>
                  <td>{group.description}</td>
                  <td>{group.createdDate ? <TextFormat type="date" value={group.createdDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    {group.users
                      ? group.users.map((val, j) => (
                          <span key={j}>
                            {val.id}
                            {j === group.users.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/group/${group.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/group/${group.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/group/${group.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Groups found</div>
        )}
      </div>
    </div>
  );
};

export default Group;
