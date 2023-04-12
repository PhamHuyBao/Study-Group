import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Group from './group';
import GroupDetail from './group-detail';
import GroupUpdate from './group-update';
import GroupDeleteDialog from './group-delete-dialog';

const GroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Group />} />
    <Route path="new" element={<GroupUpdate />} />
    <Route path=":id">
      <Route index element={<GroupDetail />} />
      <Route path="edit" element={<GroupUpdate />} />
      <Route path="delete" element={<GroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GroupRoutes;
