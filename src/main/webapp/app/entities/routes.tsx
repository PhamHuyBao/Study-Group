import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Group from './group';
import Meeting from './meeting';
import MeetingNote from './meeting-note';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="group/*" element={<Group />} />
        <Route path="meeting/*" element={<Meeting />} />
        <Route path="meeting-note/*" element={<MeetingNote />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
