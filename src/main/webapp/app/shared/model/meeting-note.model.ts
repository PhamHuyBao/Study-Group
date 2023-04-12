import { IUser } from 'app/shared/model/user.model';
import { IMeeting } from 'app/shared/model/meeting.model';

export interface IMeetingNote {
  id?: number;
  detail?: string | null;
  user?: IUser | null;
  meeting?: IMeeting | null;
}

export const defaultValue: Readonly<IMeetingNote> = {};
