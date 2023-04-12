import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './group.reducer';

export const GroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const groupEntity = useAppSelector(state => state.group.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupDetailsHeading">Group</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{groupEntity.id}</dd>
          <dt>
            <span id="groupname">Groupname</span>
          </dt>
          <dd>{groupEntity.groupname}</dd>
          <dt>
            <span id="groupOwnerId">Group Owner Id</span>
          </dt>
          <dd>{groupEntity.groupOwnerId}</dd>
          <dt>
            <span id="category">Category</span>
          </dt>
          <dd>{groupEntity.category}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{groupEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{groupEntity.description}</dd>
          <dt>
            <span id="createdDate">Created Date</span>
          </dt>
          <dd>
            {groupEntity.createdDate ? <TextFormat value={groupEntity.createdDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>
            {groupEntity.users
              ? groupEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {groupEntity.users && i === groupEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group/${groupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupDetail;
