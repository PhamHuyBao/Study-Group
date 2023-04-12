import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IGroup } from 'app/shared/model/group.model';
import { IMeetingNote } from 'app/shared/model/meeting-note.model';

export interface IMeeting {
  id?: string;
  createdDate?: string | null;
  topic?: string | null;
  description?: string | null;
  meetingLink?: string | null;
  user?: IUser | null;
  group?: IGroup | null;
  meetingNote?: IMeetingNote | null;
}

export const defaultValue: Readonly<IMeeting> = {};
