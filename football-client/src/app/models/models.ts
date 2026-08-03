export interface TeamRef {
  id: string;
  name: string;
  shortName: string | null;
  code: string | null;
  logo: string | null;
  kitPrimaryColor: string | null;
  countryIso2: string | null;
  countryFlag: string | null;
}

export interface PlayerRef {
  id: string;
  fullName: string;
  firstName: string | null;
  lastName: string;
  position: string;
  photo: string | null;
  preferredFoot: string | null;
}

export interface SeasonRef {
  id: string;
  name: string;
  yearStart: number;
  yearEnd: number;
  startDate: string;
  endDate: string;
  current: boolean;
  status: string;
}

export interface CompetitionRef {
  id: string;
  name: string;
  shortName: string | null;
  type: string;
  gender: string;
  ageLevel: string;
  sport: string;
  logo: string | null;
  level: number | null;
  countryName: string | null;
  countryIso2: string | null;
  countryFlag: string | null;
  confederationName: string | null;
  confederationAcronym: string | null;
  currentSeason: SeasonRef | null;
}

export interface MatchCard {
  id: string;
  kickoff: string;
  status: string;
  period: string | null;
  minute: number | null;
  minuteExtra: number | null;
  homeScore: number | null;
  awayScore: number | null;
  homeHtScore: number | null;
  awayHtScore: number | null;
  homeEtScore: number | null;
  awayEtScore: number | null;
  homePenaltyScore: number | null;
  awayPenaltyScore: number | null;
  homePenaltyForm: string | null;
  awayPenaltyForm: string | null;
  homeTeam: TeamRef;
  awayTeam: TeamRef;
  competition: CompetitionRef;
  season: SeasonRef | null;
  stageName: string | null;
  groupName: string | null;
  roundName: string | null;
  roundNumber: number | null;
  stadiumName: string | null;
}

export interface MatchEventItem {
  id: string;
  minute: number;
  extraMinute: number | null;
  period: string;
  team: TeamRef | null;
  player: PlayerRef | null;
  relatedPlayer: PlayerRef | null;
  eventType: string;
  detail: string | null;
  comments: string | null;
  varReviewed: boolean;
}

export interface TeamStatItem {
  team: TeamRef;
  possession: number | null;
  shots: number;
  shotsOnTarget: number;
  shotsOffTarget: number;
  shotsBlocked: number;
  corners: number;
  freeKicks: number;
  goalKicks: number;
  throwIns: number;
  offsides: number;
  fouls: number;
  yellowCards: number;
  yellowRedCards: number;
  redCards: number;
  passes: number;
  passesAccurate: number;
  tackles: number;
  interceptions: number;
  clearances: number;
  saves: number;
  xg: number | null;
  xga: number | null;
}

export interface PlayerStatItem {
  player: PlayerRef;
  team: TeamRef;
  minutesPlayed: number;
  goals: number;
  assists: number;
  shots: number;
  shotsOnTarget: number;
  xg: number | null;
  keyPasses: number;
  passes: number;
  passesAccurate: number;
  longBalls: number;
  crosses: number;
  dribblesAttempted: number;
  dribblesSucceeded: number;
  tackles: number;
  interceptions: number;
  clearances: number;
  foulsCommitted: number;
  foulsDrawn: number;
  yellowCards: number;
  redCards: number;
  saves: number;
  goalsConceded: number;
  rating: number | null;
}

export interface LineupItem {
  team: TeamRef;
  player: PlayerRef;
  starter: boolean;
  captain: boolean;
  shirtNumber: number | null;
  position: string | null;
  positionX: number | null;
  positionY: number | null;
  formationSlot: number | null;
}

export interface StandingItem {
  id: string;
  rankPosition: number;
  team: TeamRef;
  played: number;
  wins: number;
  draws: number;
  losses: number;
  goalsFor: number;
  goalsAgainst: number;
  goalDifference: number;
  points: number;
  form: string | null;
  groupId: string | null;
  groupName: string | null;
}

export interface PlayerSeasonStatItem {
  player: PlayerRef;
  team: TeamRef;
  appearances: number;
  appearancesAsStarter: number;
  minutesPlayed: number;
  goals: number;
  assists: number;
  shots: number;
  shotsOnTarget: number;
  yellowCards: number;
  redCards: number;
  saves: number;
  cleanSheets: number;
  avgRating: number | null;
}

export interface SquadPlayerItem {
  id: string;
  fullName: string;
  firstName: string | null;
  lastName: string;
  position: string;
  photo: string | null;
  preferredFoot: string | null;
  shirtNumber: number | null;
  captain: boolean;
  status: string;
  nationalityName: string | null;
  nationalityFlag: string | null;
}

export interface TeamSuspensionItem {
  id: string;
  player: PlayerRef;
  cardType: string;
  reason: string | null;
  startDate: string;
  endDate: string | null;
  matchesBanned: number;
  matchesRemaining: number;
  status: string;
  competitionName: string | null;
  seasonName: string | null;
}

export interface TeamInjuryItem {
  id: string;
  player: PlayerRef;
  injuryType: string;
  bodyPart: string | null;
  severity: string;
  startDate: string;
  expectedReturn: string | null;
  actualReturn: string | null;
  status: string;
}

export interface NewsItem {
  id: string;
  title: string;
  excerpt: string | null;
  content: string;
  image: string | null;
  author: string | null;
  publishedAt: string;
  competitionName: string | null;
  team: TeamRef | null;
  player: PlayerRef | null;
}

export interface RefereeItem {
  id: string;
  fullName: string;
  photo: string | null;
  category: string;
  countryName: string | null;
  countryFlag: string | null;
  matchesCount: number;
  roles: string[];
}

export interface CoachItem {
  id: string;
  fullName: string;
  photo: string | null;
  role: string;
  team: TeamRef;
  startDate: string;
  endDate: string | null;
  interim: boolean;
}

export interface TeamDetail {
  id: string;
  name: string;
  shortName: string | null;
  code: string | null;
  founded: number | null;
  logo: string | null;
  kitPrimaryColor: string | null;
  kitSecondaryColor: string | null;
  website: string | null;
  description: string | null;
  countryName: string | null;
  countryIso2: string | null;
  countryFlag: string | null;
  stadiumName: string | null;
  stadiumCity: string | null;
  stadiumCapacity: number | null;
}

export interface MatchDetail {
  match: MatchCard;
  refereeName: string | null;
  stadiumCity: string | null;
  attendance: number | null;
  weather: string | null;
  temperature: number | null;
  windSpeed: number | null;
  note: string | null;
  homePenaltyForm: string | null;
  awayPenaltyForm: string | null;
  firstHalfStart: string | null;
  secondHalfStart: string | null;
  extraTimeStart: string | null;
  penaltyShootoutStart: string | null;
  events: MatchEventItem[];
  teamStats: TeamStatItem[];
  playerStats: PlayerStatItem[];
  lineups: LineupItem[];
}

export interface CompetitionDetail {
  competition: CompetitionRef;
  seasons: SeasonRef[];
}

export interface PageInfo<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface LiveScoreMessage {
  matchId: string;
  homeTeamId: string | null;
  awayTeamId: string | null;
  status: string;
  period: string | null;
  minute: number | null;
  minuteExtra: number | null;
  homeScore: number | null;
  awayScore: number | null;
  homeHtScore: number | null;
  awayHtScore: number | null;
  homeEtScore: number | null;
  awayEtScore: number | null;
  homePenaltyScore: number | null;
  awayPenaltyScore: number | null;
  updatedAt: string;
}

export interface LiveEventMessage {
  id: string;
  matchId: string;
  minute: number;
  extraMinute: number | null;
  period: string;
  teamId: string | null;
  playerId: string | null;
  relatedPlayerId: string | null;
  eventType: string;
  detail: string | null;
  comments: string | null;
  varReviewed: boolean;
}

export interface LiveStandingMessage {
  id: string;
  seasonId: string | null;
  stageId: string | null;
  groupId: string | null;
  teamId: string | null;
  rankPosition: number;
  played: number;
  wins: number;
  draws: number;
  losses: number;
  goalsFor: number;
  goalsAgainst: number;
  goalDifference: number;
  points: number;
  form: string | null;
}
