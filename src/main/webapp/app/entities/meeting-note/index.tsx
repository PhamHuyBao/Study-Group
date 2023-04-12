import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MeetingNote from './meeting-note';
import MeetingNoteDetail from './meeting-note-detail';
import MeetingNoteUpdate from './meeting-note-update';
import MeetingNoteDeleteDialog from './meeting-note-delete-dialog';

const MeetingNoteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MeetingNote />} />
    <Route path="new" element={<MeetingNoteUpdate />} />
    <Route path=":id">
      <Route index element={<MeetingNoteDetail />} />
      <Route path="edit" element={<MeetingNoteUpdate />} />
      <Route path="delete" element={<MeetingNoteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MeetingNoteRoutes;
