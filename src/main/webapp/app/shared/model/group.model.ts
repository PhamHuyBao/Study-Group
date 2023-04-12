import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IMeeting } from 'app/shared/model/meeting.model';

export interface IGroup {
  id?: number;
  groupname?: string | null;
  groupOwnerId?: string | null;
  category?: string | null;
  status?: boolean | null;
  description?: string | null;
  createdDate?: string | null;
  users?: IUser[] | null;
  meeting?: IMeeting | null;
}

export const defaultValue: Readonly<IGroup> = {
  status: false,
};
