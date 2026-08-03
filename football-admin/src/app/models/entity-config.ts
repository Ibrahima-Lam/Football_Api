export interface ColumnDef {
  header: string;
  field: string;
  sortable?: boolean;
  type?: 'text' | 'number' | 'date' | 'boolean';
}

export interface FormFieldDef {
  name: string;
  label: string;
  type: 'text' | 'number' | 'email' | 'date' | 'datetime-local' | 'select' | 'boolean' | 'textarea' | 'password' | 'url' | 'file';
  required?: boolean;
  placeholder?: string;
  min?: number;
  max?: number;
  step?: number;
  minLength?: number;
  maxLength?: number;
  options?: { label: string; value: any }[];
  resource?: string;
  displayField?: string;
  valueField?: string;
  fileKind?: 'image' | 'video';
  colSpan?: number;
}

export interface FilterDef {
  name: string;
  label: string;
  type?: string;
  options?: { label: string; value: any }[];
  resource?: string;
  displayField?: string;
  valueField?: string;
}

export interface EntityConfig {
  title: string;
  resource: string;
  columns: ColumnDef[];
  formFields: FormFieldDef[];
  quickFields?: FormFieldDef[];
  idField: string;
  itemName: string;
}

export const COMPETITION_TYPES = ['LEAGUE', 'LEAGUE_CUP', 'CUP', 'PLAYOFFS', 'SUPER_CUP', 'INTERNATIONAL', 'FRIENDLY'];
export const GENDERS = ['male', 'female', 'mixed'];
export const SPORTS = ['football', 'futsal', 'beach_soccer'];
export const AGE_LEVELS = ['senior', 'U23', 'U20', 'U17'];
export const SEASON_STATUSES = ['UPCOMING', 'ONGOING', 'FINISHED'];
export const STAGE_TYPES = ['GROUP', 'KNOCKOUT', 'LEAGUE'];
export const QUALIFICATION_TYPES = ['PENDING', 'QUALIFIED', 'ELIMINATED'];
export const ROUND_TYPES = ['REGULAR', 'PLAYOFF', 'RELEGATION', 'FINAL'];
export const TEAM_TYPES = ['MEN_CLUB', 'MEN_NATIONAL', 'WOMEN_CLUB', 'WOMEN_NATIONAL', 'MIXED_CLUB', 'REGIONAL'];
export const SURFACES = ['grass', 'artificial', 'hybrid'];
export const REFEREE_CATEGORIES = ['FIFA', 'NATIONAL', 'REGIONAL'];
export const COACH_ROLES = ['HEAD_COACH', 'ASSISTANT_COACH', 'GOALKEEPER_COACH', 'FITNESS_COACH'];
export const FOOT_PREFERENCES = ['left', 'right', 'both'];
export const PLAYER_POSITIONS = ['GK', 'CB', 'LB', 'RB', 'CM', 'CAM', 'CDM', 'LW', 'RW', 'ST'];
export const PLAYER_STATUSES = ['ACTIVE', 'RETIRED', 'DECEASED'];
export const TRANSFER_TYPES = ['PERMANENT', 'LOAN', 'FREE', 'RETURN_FROM_LOAN'];
export const MATCH_STATUSES = ['SCHEDULED', 'TIMED', 'IN_PLAY', 'PAUSED', 'EXTRA_TIME', 'PENALTY_SHOOTOUT', 'FINISHED', 'SUSPENDED', 'POSTPONED', 'CANCELLED', 'ABANDONED', 'AWARDED'];
export const MATCH_PERIODS = ['1H', 'HT', '2H', 'ET1', 'ET_HT', 'ET2', 'PEN', 'FT'];
export const EVENT_TYPES = ['GOAL', 'PENALTY_MISS', 'YELLOW_CARD', 'YELLOW_RED', 'RED_CARD', 'SUBSTITUTION', 'VAR_DECISION', 'INJURY'];
export const SHOOTOUT_STATUSES = ['SCORED', 'MISSED', 'SAVED'];
export const MATCH_REFEREE_ROLES = ['REFEREE', 'ASSISTANT_REFEREE_N1', 'ASSISTANT_REFEREE_N2', 'ASSISTANT_REFEREE_N3', 'FOURTH_OFFICIAL', 'VIDEO_ASSISTANT_REFEREE_N1', 'VIDEO_ASSISTANT_REFEREE_N2', 'VIDEO_ASSISTANT_REFEREE_N3'];
export const ENTRY_TYPES = ['PROMOTED', 'RELEGATED', 'CHAMPION', 'WILD_CARD', 'REGULAR'];
export const SEASON_OUTCOMES = ['CHAMPION', 'PROMOTED', 'RELEGATED', 'PLAYOFF', 'QUALIFIED_UCL', 'QUALIFIED_UEL', 'QUALIFIED_UECL', 'REMAINED', 'WITHDRAWN'];
export const REGISTRATION_STATUSES = ['REGISTERED', 'LOANED_OUT', 'INELIGIBLE', 'UNREGISTERED'];
export const TROPHY_TYPES = ['TEAM', 'INDIVIDUAL'];
export const INJURY_SEVERITIES = ['MINOR', 'MODERATE', 'SEVERE'];
export const INJURY_STATUSES = ['ACTIVE', 'RECOVERED'];
export const SUSPENSION_CARD_TYPES = ['RED_CARD', 'SECOND_YELLOW', 'CUMULATIVE'];
export const SUSPENSION_STATUSES = ['ACTIVE', 'SERVED'];
export const MEDIA_TYPES = ['PHOTO', 'VIDEO', 'HIGHLIGHT'];
export const MEDIA_ENTITY_TYPES = ['match', 'team', 'player', 'competition', 'stadium'];
export const SPONSOR_ENTITY_TYPES = ['competition', 'match', 'team', 'player', 'referee', 'coach'];
export const API_PLANS = ['FREE', 'STARTER', 'PRO', 'ENTERPRISE'];
export const HTTP_METHODS = ['GET', 'POST', 'PUT', 'DELETE', 'PATCH'];

const boolSelect = (label: string, name: string): FormFieldDef => ({
  name, label, type: 'select', required: true,
  options: [{ label: 'Yes', value: true }, { label: 'No', value: false }]
});

const boolToggle = (label: string, name: string): FormFieldDef => ({
  name, label, type: 'boolean'
});

const fk = (name: string, label: string, resource: string, displayField: string = 'name', required: boolean = false): FormFieldDef => ({
  name, label, type: 'select', resource, displayField, valueField: 'id', required
});

const enumSelect = (label: string, name: string, values: string[], required: boolean = true): FormFieldDef => ({
  name, label, type: 'select', required,
  options: values.map(v => ({ label: v, value: v }))
});

export const ENTITY_CONFIGS: Record<string, EntityConfig> = {
  continents: {
    title: 'Continents', resource: 'continents', idField: 'id', itemName: 'continent',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Code', field: 'code' }, { header: 'Name', field: 'name' }],
    formFields: [
      { name: 'code', label: 'Code', type: 'text', required: true, maxLength: 10 },
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 100 }
    ]
  },
  countries: {
    title: 'Countries', resource: 'countries', idField: 'id', itemName: 'country',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Continent', field: 'continent.name' }, { header: 'ISO2', field: 'iso2' }, { header: 'ISO3', field: 'iso3' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 100 },
      fk('continentId', 'Continent', 'continents'),
      { name: 'officialName', label: 'Official Name', type: 'text', maxLength: 200 },
      { name: 'iso2', label: 'ISO2', type: 'text', required: true, maxLength: 2 },
      { name: 'iso3', label: 'ISO3', type: 'text', required: true, maxLength: 3 },
      { name: 'fifaCode', label: 'FIFA Code', type: 'text', maxLength: 3 },
      { name: 'flagUrl', label: 'Flag URL', type: 'file', fileKind: 'image' }
    ]
  },
  cities: {
    title: 'Cities', resource: 'cities', idField: 'id', itemName: 'city',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Country', field: 'country.name' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      fk('countryId', 'Country', 'countries'),
      { name: 'latitude', label: 'Latitude', type: 'number', step: 0.000001 },
      { name: 'longitude', label: 'Longitude', type: 'number', step: 0.000001 },
      { name: 'timezone', label: 'Timezone', type: 'text', maxLength: 100 }
    ]
  },
  confederations: {
    title: 'Confederations', resource: 'confederations', idField: 'id', itemName: 'confederation',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Acronym', field: 'acronym' }, { header: 'Continent', field: 'continent.name' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      fk('continentId', 'Continent', 'continents'),
      { name: 'acronym', label: 'Acronym', type: 'text', required: true, maxLength: 20 },
      { name: 'logo', label: 'Logo', type: 'file', fileKind: 'image' },
      { name: 'website', label: 'Website', type: 'url' },
      { name: 'founded', label: 'Founded', type: 'number' },
      { name: 'headquarters', label: 'Headquarters', type: 'text', maxLength: 200 }
    ]
  },
  competitions: {
    title: 'Competitions', resource: 'competitions', idField: 'id', itemName: 'competition',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Type', field: 'type' }, { header: 'Gender', field: 'gender' }, { header: 'Sport', field: 'sport' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      { name: 'shortName', label: 'Short Name', type: 'text', maxLength: 100 },
      fk('countryId', 'Country', 'countries'),
      fk('confederationId', 'Confederation', 'confederations'),
      enumSelect('Type', 'type', COMPETITION_TYPES),
      enumSelect('Gender', 'gender', GENDERS),
      enumSelect('Age Level', 'ageLevel', AGE_LEVELS),
      enumSelect('Sport', 'sport', SPORTS),
      { name: 'level', label: 'Level', type: 'number' },
      { name: 'logo', label: 'Logo', type: 'file', fileKind: 'image' },
      { name: 'founded', label: 'Founded', type: 'number' },
      { name: 'website', label: 'Website', type: 'url' },
      boolSelect('Active', 'active')
    ]
  },
  seasons: {
    title: 'Seasons', resource: 'seasons', idField: 'id', itemName: 'season',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Year', field: 'yearStart', type: 'number' }, { header: 'Competition', field: 'competition.name' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 100 },
      fk('competitionId', 'Competition', 'competitions'),
      { name: 'yearStart', label: 'Year Start', type: 'number', required: true },
      { name: 'yearEnd', label: 'Year End', type: 'number' },
      { name: 'startDate', label: 'Start Date', type: 'date', required: true },
      { name: 'endDate', label: 'End Date', type: 'date', required: true },
      boolSelect('Current', 'current'),
      enumSelect('Status', 'status', SEASON_STATUSES)
    ]
  },
  stages: {
    title: 'Stages', resource: 'stages', idField: 'id', itemName: 'stage',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Type', field: 'type' }, { header: 'Order', field: 'orderNo', type: 'number' }],
    formFields: [
      fk('seasonId', 'Season', 'seasons'),
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 100 },
      enumSelect('Type', 'type', STAGE_TYPES),
      { name: 'orderNo', label: 'Order No', type: 'number', required: true }
    ]
  },
  groups: {
    title: 'Groups', resource: 'groups', idField: 'id', itemName: 'group',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Stage', field: 'stage.name' }],
    formFields: [
      fk('stageId', 'Stage', 'stages'),
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 50 }
    ]
  },
  rounds: {
    title: 'Rounds', resource: 'rounds', idField: 'id', itemName: 'round',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Number', field: 'number', type: 'number' }, { header: 'Type', field: 'type' }, { header: 'Status', field: 'status' }],
    formFields: [
      fk('stageId', 'Stage', 'stages'),
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 100 },
      { name: 'slug', label: 'Slug', type: 'text', required: true, maxLength: 150 },
      { name: 'number', label: 'Number', type: 'number', required: true },
      enumSelect('Type', 'type', ROUND_TYPES),
      boolSelect('Current', 'current'),
      enumSelect('Status', 'status', SEASON_STATUSES),
      { name: 'startDate', label: 'Start Date', type: 'date' },
      { name: 'endDate', label: 'End Date', type: 'date' }
    ]
  },
  groupteams: {
    title: 'Group Teams', resource: 'groupteams', idField: 'id', itemName: 'group team',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Group', field: 'group.name' }, { header: 'Team', field: 'team.name' }, { header: 'Seed', field: 'seed', type: 'number' }, { header: 'Pot', field: 'pot', type: 'number' }, { header: 'Qualification', field: 'qualification' }],
    formFields: [
      fk('groupId', 'Group', 'groups'),
      fk('teamId', 'Team', 'teams'),
      { name: 'seed', label: 'Seed', type: 'number' },
      { name: 'pot', label: 'Pot', type: 'number' },
      { name: 'qualifiedFrom', label: 'Qualified From', type: 'text', maxLength: 100 },
      enumSelect('Qualification', 'qualification', QUALIFICATION_TYPES)
    ]
  },
  teamseasonparticipations: {
    title: 'Team Season Participations', resource: 'teamseasonparticipations', idField: 'id', itemName: 'participation',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Season', field: 'season.name' }, { header: 'Team', field: 'team.name' }, { header: 'Entry Type', field: 'entryType' }, { header: 'Outcome', field: 'outcome' }],
    formFields: [
      fk('seasonId', 'Season', 'seasons'),
      fk('teamId', 'Team', 'teams'),
      enumSelect('Entry Type', 'entryType', ENTRY_TYPES),
      fk('entryFromCompetitionId', 'Entry From Competition', 'competitions'),
      { name: 'finalRank', label: 'Final Rank', type: 'number' },
      enumSelect('Outcome', 'outcome', SEASON_OUTCOMES, false),
      boolSelect('Withdrawn', 'withdrawn'),
      { name: 'withdrawalDate', label: 'Withdrawal Date', type: 'date' }
    ]
  },
  teams: {
    title: 'Teams', resource: 'teams', idField: 'id', itemName: 'team',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Type', field: 'type' }, { header: 'Country', field: 'country.name' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      { name: 'shortName', label: 'Short Name', type: 'text', maxLength: 100 },
      enumSelect('Type', 'type', TEAM_TYPES),
      boolSelect('National Team', 'nationalTeam'),
      fk('countryId', 'Country', 'countries'),
      fk('stadiumId', 'Stadium', 'stadiums', 'name'),
      { name: 'code', label: 'Code', type: 'text', maxLength: 20 },
      { name: 'founded', label: 'Founded', type: 'number' },
      { name: 'logo', label: 'Logo', type: 'file', fileKind: 'image' },
      { name: 'kitPrimaryColor', label: 'Kit Primary Color', type: 'text', maxLength: 7 },
      { name: 'kitSecondaryColor', label: 'Kit Secondary Color', type: 'text', maxLength: 7 },
      { name: 'website', label: 'Website', type: 'url' },
      { name: 'address', label: 'Address', type: 'textarea' },
      { name: 'phone', label: 'Phone', type: 'text', maxLength: 50 },
      { name: 'email', label: 'Email', type: 'email' },
      { name: 'description', label: 'Description', type: 'textarea' },
      boolSelect('Active', 'active')
    ]
  },
  stadiums: {
    title: 'Stadiums', resource: 'stadiums', idField: 'id', itemName: 'stadium',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Country', field: 'country.name' }, { header: 'City', field: 'city.name' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      fk('countryId', 'Country', 'countries'),
      fk('cityId', 'City', 'cities'),
      { name: 'capacity', label: 'Capacity', type: 'number' },
      enumSelect('Surface', 'surface', SURFACES, false),
      { name: 'latitude', label: 'Latitude', type: 'number', step: 0.000001 },
      { name: 'longitude', label: 'Longitude', type: 'number', step: 0.000001 },
      { name: 'address', label: 'Address', type: 'textarea' },
      { name: 'opened', label: 'Opened', type: 'number' },
      { name: 'image', label: 'Image', type: 'file', fileKind: 'image' }
    ]
  },
  referees: {
    title: 'Referees', resource: 'referees', idField: 'id', itemName: 'referee',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'fullName' }, { header: 'Category', field: 'category' }, { header: 'Country', field: 'country.name' }],
    formFields: [
      fk('countryId', 'Country', 'countries'),
      { name: 'firstName', label: 'First Name', type: 'text', maxLength: 100 },
      { name: 'lastName', label: 'Last Name', type: 'text', required: true, maxLength: 100 },
      { name: 'fullName', label: 'Full Name', type: 'text', required: true, maxLength: 200 },
      { name: 'birthDate', label: 'Birth Date', type: 'date' },
      { name: 'photo', label: 'Photo', type: 'file', fileKind: 'image' },
      enumSelect('Category', 'category', REFEREE_CATEGORIES),
      boolSelect('Active', 'active')
    ]
  },
  coaches: {
    title: 'Coaches', resource: 'coaches', idField: 'id', itemName: 'coach',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'fullName' }, { header: 'Role', field: 'role' }, { header: 'Country', field: 'country.name' }],
    formFields: [
      fk('countryId', 'Country', 'countries'),
      { name: 'firstName', label: 'First Name', type: 'text', maxLength: 100 },
      { name: 'lastName', label: 'Last Name', type: 'text', required: true, maxLength: 100 },
      { name: 'fullName', label: 'Full Name', type: 'text', required: true, maxLength: 200 },
      { name: 'birthDate', label: 'Birth Date', type: 'date' },
      { name: 'photo', label: 'Photo', type: 'file', fileKind: 'image' },
      enumSelect('Role', 'role', COACH_ROLES),
      boolSelect('Active', 'active')
    ]
  },
  teamcoachs: {
    title: 'Team Coaches', resource: 'teamcoachs', idField: 'id', itemName: 'team coach',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Team', field: 'team.name' }, { header: 'Coach', field: 'coach.fullName' }, { header: 'Season', field: 'season.name' }, { header: 'Role', field: 'role' }, { header: 'Start', field: 'startDate', type: 'date' }],
    formFields: [
      fk('teamId', 'Team', 'teams'),
      fk('coachId', 'Coach', 'coaches', 'fullName'),
      fk('seasonId', 'Season', 'seasons'),
      enumSelect('Role', 'role', COACH_ROLES),
      { name: 'startDate', label: 'Start Date', type: 'date', required: true },
      { name: 'endDate', label: 'End Date', type: 'date' },
      boolSelect('Interim', 'interim')
    ]
  },
  players: {
    title: 'Players', resource: 'players', idField: 'id', itemName: 'player',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'fullName' }, { header: 'Position', field: 'position' }, { header: 'Country', field: 'country.name' }],
    formFields: [
      { name: 'fullName', label: 'Full Name', type: 'text', required: true, maxLength: 200 },
      fk('countryId', 'Country', 'countries'),
      fk('nationalityId', 'Nationality', 'countries'),
      fk('secondNationalityId', 'Second Nationality', 'countries'),
      { name: 'firstName', label: 'First Name', type: 'text', maxLength: 100 },
      { name: 'lastName', label: 'Last Name', type: 'text', required: true, maxLength: 100 },
      { name: 'birthDate', label: 'Birth Date', type: 'date', required: true },
      { name: 'birthPlace', label: 'Birth Place', type: 'text', maxLength: 100 },
      { name: 'height', label: 'Height (cm)', type: 'number', step: 0.01 },
      { name: 'weight', label: 'Weight (kg)', type: 'number', step: 0.01 },
      enumSelect('Preferred Foot', 'preferredFoot', FOOT_PREFERENCES, false),
      enumSelect('Position', 'position', PLAYER_POSITIONS),
      { name: 'photo', label: 'Photo', type: 'file', fileKind: 'image' },
      { name: 'marketValue', label: 'Market Value', type: 'number', step: 0.01 },
      enumSelect('Status', 'status', PLAYER_STATUSES),
      { name: 'twitter', label: 'Twitter', type: 'url' },
      { name: 'instagram', label: 'Instagram', type: 'url' }
    ]
  },
  playerseasonregistrations: {
    title: 'Player Season Registrations', resource: 'playerseasonregistrations', idField: 'id', itemName: 'registration',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Player', field: 'player.fullName' }, { header: 'Team', field: 'team.name' }, { header: 'Season', field: 'season.name' }, { header: 'Position', field: 'position' }, { header: 'Status', field: 'status' }],
    formFields: [
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('teamId', 'Team', 'teams'),
      fk('seasonId', 'Season', 'seasons'),
      fk('competitionId', 'Competition', 'competitions'),
      { name: 'shirtNumber', label: 'Shirt Number', type: 'number' },
      enumSelect('Position', 'position', PLAYER_POSITIONS, false),
      enumSelect('Status', 'status', REGISTRATION_STATUSES),
      { name: 'registeredAt', label: 'Registered At', type: 'date' },
      { name: 'unregisteredAt', label: 'Unregistered At', type: 'date' },
      boolSelect('Captain', 'captain')
    ]
  },
  playerseasonstats: {
    title: 'Player Season Stats', resource: 'playerseasonstats', idField: 'id', itemName: 'stat line',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Player', field: 'player.fullName' }, { header: 'Apps', field: 'appearances', type: 'number' }, { header: 'Goals', field: 'goals', type: 'number' }, { header: 'Assists', field: 'assists', type: 'number' }],
    formFields: [
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('teamId', 'Team', 'teams'),
      fk('seasonId', 'Season', 'seasons'),
      fk('competitionId', 'Competition', 'competitions'),
      { name: 'appearances', label: 'Appearances', type: 'number', required: true },
      { name: 'appearancesAsStarter', label: 'Appearances as Starter', type: 'number', required: true },
      { name: 'minutesPlayed', label: 'Minutes Played', type: 'number', required: true },
      { name: 'goals', label: 'Goals', type: 'number', required: true },
      { name: 'assists', label: 'Assists', type: 'number', required: true },
      { name: 'shots', label: 'Shots', type: 'number', required: true },
      { name: 'shotsOnTarget', label: 'Shots on Target', type: 'number', required: true },
      { name: 'xg', label: 'Expected Goals (xG)', type: 'number', step: 0.001 },
      { name: 'keyPasses', label: 'Key Passes', type: 'number', required: true },
      { name: 'passes', label: 'Passes', type: 'number', required: true },
      { name: 'passesAccurate', label: 'Accurate Passes', type: 'number', required: true },
      { name: 'dribblesAttempted', label: 'Dribbles Attempted', type: 'number', required: true },
      { name: 'dribblesSucceeded', label: 'Dribbles Succeeded', type: 'number', required: true },
      { name: 'tackles', label: 'Tackles', type: 'number', required: true },
      { name: 'interceptions', label: 'Interceptions', type: 'number', required: true },
      { name: 'foulsCommitted', label: 'Fouls Committed', type: 'number', required: true },
      { name: 'foulsDrawn', label: 'Fouls Drawn', type: 'number', required: true },
      { name: 'yellowCards', label: 'Yellow Cards', type: 'number', required: true },
      { name: 'redCards', label: 'Red Cards', type: 'number', required: true },
      { name: 'saves', label: 'Saves', type: 'number', required: true },
      { name: 'goalsConceded', label: 'Goals Conceded', type: 'number', required: true },
      { name: 'cleanSheets', label: 'Clean Sheets', type: 'number', required: true },
      { name: 'avgRating', label: 'Average Rating', type: 'number', step: 0.01 }
    ]
  },
  contracts: {
    title: 'Contracts', resource: 'contracts', idField: 'id', itemName: 'contract',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Player', field: 'player.fullName' }, { header: 'Team', field: 'team.name' }, { header: 'Start', field: 'startDate', type: 'date' }, { header: 'End', field: 'endDate', type: 'date' }],
    formFields: [
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('teamId', 'Team', 'teams'),
      { name: 'startDate', label: 'Start Date', type: 'date', required: true },
      { name: 'endDate', label: 'End Date', type: 'date', required: true },
      { name: 'salary', label: 'Salary', type: 'number', step: 0.01 },
      { name: 'shirtNumber', label: 'Shirt Number', type: 'number' },
      boolSelect('Current', 'current')
    ]
  },
  transfers: {
    title: 'Transfers', resource: 'transfers', idField: 'id', itemName: 'transfer',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Player', field: 'player.fullName' }, { header: 'Date', field: 'transferDate', type: 'date' }, { header: 'Fee', field: 'fee', type: 'number' }, { header: 'Type', field: 'transferType' }],
    formFields: [
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('fromTeamId', 'From Team', 'teams'),
      fk('toTeamId', 'To Team', 'teams'),
      fk('seasonId', 'Season', 'seasons'),
      { name: 'transferDate', label: 'Transfer Date', type: 'date', required: true },
      { name: 'fee', label: 'Fee', type: 'number', step: 0.01 },
      { name: 'currency', label: 'Currency', type: 'text', maxLength: 3 },
      enumSelect('Transfer Type', 'transferType', TRANSFER_TYPES)
    ]
  },
  matches: {
    title: 'Matches', resource: 'matches', idField: 'id', itemName: 'match',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Kickoff', field: 'kickoff', type: 'date' }, { header: 'Home', field: 'homeTeam.name' }, { header: 'Away', field: 'awayTeam.name' }, { header: 'Status', field: 'status' }],
    formFields: [
      fk('seasonId', 'Season', 'seasons', 'name', true),
      fk('stageId', 'Stage', 'stages', 'name', true),
      fk('roundId', 'Round', 'rounds', 'name', true),
      fk('groupId', 'Group', 'groups'),
      fk('homeTeamId', 'Home Team', 'teams', 'name', true),
      fk('awayTeamId', 'Away Team', 'teams', 'name', true),
      fk('stadiumId', 'Stadium', 'stadiums', 'name'),
      fk('refereeId', 'Referee', 'referees', 'fullName'),
      { name: 'kickoff', label: 'Kickoff', type: 'datetime-local', required: true },
      enumSelect('Status', 'status', MATCH_STATUSES),
      enumSelect('Period', 'period', MATCH_PERIODS, false),
      { name: 'minute', label: 'Minute', type: 'number' },
      { name: 'minuteExtra', label: 'Extra Minutes', type: 'number' },
      { name: 'homeScore', label: 'Home Score', type: 'number' },
      { name: 'awayScore', label: 'Away Score', type: 'number' },
      { name: 'homeHtScore', label: 'Home HT Score', type: 'number' },
      { name: 'awayHtScore', label: 'Away HT Score', type: 'number' },
      { name: 'homeEtScore', label: 'Home ET Score', type: 'number' },
      { name: 'awayEtScore', label: 'Away ET Score', type: 'number' },
      { name: 'homePenaltyScore', label: 'Home Penalty Score', type: 'number' },
      { name: 'awayPenaltyScore', label: 'Away Penalty Score', type: 'number' },
      { name: 'homePenaltyForm', label: 'Home Penalty Form', type: 'text', maxLength: 30 },
      { name: 'awayPenaltyForm', label: 'Away Penalty Form', type: 'text', maxLength: 30 },
      { name: 'attendance', label: 'Attendance', type: 'number' },
      { name: 'weather', label: 'Weather', type: 'text', maxLength: 100 },
      { name: 'temperature', label: 'Temperature (°C)', type: 'number', step: 0.1 },
      { name: 'windSpeed', label: 'Wind Speed (km/h)', type: 'number', step: 0.1 },
      { name: 'note', label: 'Note', type: 'textarea' },
      { name: 'firstHalfStart', label: 'First Half Start', type: 'datetime-local' },
      { name: 'secondHalfStart', label: 'Second Half Start', type: 'datetime-local' },
      { name: 'extraTimeStart', label: 'Extra Time Start', type: 'datetime-local' },
      { name: 'penaltyShootoutStart', label: 'Penalty Shootout Start', type: 'datetime-local' }
    ],
    quickFields: [
      { name: 'kickoff', label: 'Kickoff', type: 'datetime-local', colSpan: 12 },
      enumSelect('Status', 'status', MATCH_STATUSES, false),
      enumSelect('Period', 'period', MATCH_PERIODS, false),
      { name: 'minute', label: 'Minute', type: 'number' },
      { name: 'minuteExtra', label: 'Extra Minutes', type: 'number' },
      { name: 'homeScore', label: 'Home Score', type: 'number' },
      { name: 'awayScore', label: 'Away Score', type: 'number' },
      { name: 'homeHtScore', label: 'Home HT Score', type: 'number' },
      { name: 'awayHtScore', label: 'Away HT Score', type: 'number' },
      { name: 'homeEtScore', label: 'Home ET Score', type: 'number' },
      { name: 'awayEtScore', label: 'Away ET Score', type: 'number' },
      { name: 'homePenaltyScore', label: 'Home Penalty Score', type: 'number' },
      { name: 'awayPenaltyScore', label: 'Away Penalty Score', type: 'number' },
      { name: 'homePenaltyForm', label: 'Home Penalty Form', type: 'text', maxLength: 30 },
      { name: 'awayPenaltyForm', label: 'Away Penalty Form', type: 'text', maxLength: 30 }
    ]
  },
  lineups: {
    title: 'Lineups', resource: 'lineups', idField: 'id', itemName: 'lineup',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Team', field: 'team.name' }, { header: 'Player', field: 'player.fullName' }],
    formFields: [
      fk('matchId', 'Match', 'matches'),
      fk('teamId', 'Team', 'teams'),
      fk('playerId', 'Player', 'players', 'fullName'),
      boolSelect('Starter', 'starter'),
      boolSelect('Captain', 'captain'),
      { name: 'shirtNumber', label: 'Shirt Number', type: 'number' },
      { name: 'position', label: 'Position', type: 'text', maxLength: 30 },
      { name: 'positionX', label: 'Position X', type: 'number', step: 0.01 },
      { name: 'positionY', label: 'Position Y', type: 'number', step: 0.01 },
      { name: 'formationSlot', label: 'Formation Slot', type: 'number' }
    ]
  },
  matchformations: {
    title: 'Match Formations', resource: 'matchformations', idField: 'id', itemName: 'formation',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Team', field: 'team.name' }, { header: 'Formation', field: 'formation' }],
    formFields: [
      fk('matchId', 'Match', 'matches'),
      fk('teamId', 'Team', 'teams'),
      { name: 'formation', label: 'Formation', type: 'text', required: true, maxLength: 20, placeholder: '4-3-3' }
    ]
  },
  matchevents: {
    title: 'Match Events', resource: 'matchevents', idField: 'id', itemName: 'event',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Minute', field: 'minute', type: 'number' }, { header: 'Team', field: 'team.name' }, { header: 'Type', field: 'eventType' }],
    formFields: [
      fk('matchId', 'Match', 'matches'),
      { name: 'minute', label: 'Minute', type: 'number', required: true },
      { name: 'extraMinute', label: 'Extra Minute', type: 'number' },
      enumSelect('Period', 'period', MATCH_PERIODS),
      fk('teamId', 'Team', 'teams'),
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('relatedPlayerId', 'Related Player', 'players', 'fullName'),
      enumSelect('Event Type', 'eventType', EVENT_TYPES),
      { name: 'detail', label: 'Detail', type: 'text', maxLength: 100 },
      { name: 'comments', label: 'Comments', type: 'textarea' },
      boolSelect('VAR Reviewed', 'varReviewed')
    ]
  },
  matchpenaltyshootoutshots: {
    title: 'Penalty Shootout Shots', resource: 'matchpenaltyshootoutshots', idField: 'id', itemName: 'shootout shot',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Team', field: 'team.name' }, { header: 'Player', field: 'player.fullName' }, { header: 'Order', field: 'shotOrder', type: 'number' }, { header: 'Status', field: 'status' }],
    formFields: [
      fk('matchId', 'Match', 'matches'),
      fk('teamId', 'Team', 'teams'),
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('goalkeeperId', 'Goalkeeper', 'players', 'fullName'),
      { name: 'shotOrder', label: 'Shot Order', type: 'number', required: true },
      { name: 'round', label: 'Round', type: 'number', required: true },
      enumSelect('Status', 'status', SHOOTOUT_STATUSES)
    ]
  },
  matchreferees: {
    title: 'Match Referees', resource: 'matchreferees', idField: 'id', itemName: 'match referee',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Referee', field: 'referee.fullName' }, { header: 'Role', field: 'role' }],
    formFields: [
      fk('matchId', 'Match', 'matches'),
      fk('refereeId', 'Referee', 'referees', 'fullName'),
      enumSelect('Role', 'role', MATCH_REFEREE_ROLES)
    ]
  },
  matchstatisticsteams: {
    title: 'Match Statistics (Team)', resource: 'matchstatisticsteams', idField: 'id', itemName: 'team stat line',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Team', field: 'team.name' }, { header: 'Poss', field: 'possession', type: 'number' }, { header: 'Shots', field: 'shots', type: 'number' }, { header: 'Corners', field: 'corners', type: 'number' }],
    formFields: [
      fk('matchId', 'Match', 'matches'),
      fk('teamId', 'Team', 'teams'),
      { name: 'possession', label: 'Possession (%)', type: 'number', required: true, step: 0.01 },
      { name: 'shots', label: 'Shots', type: 'number', required: true },
      { name: 'shotsOnTarget', label: 'Shots on Target', type: 'number', required: true },
      { name: 'shotsOffTarget', label: 'Shots Off Target', type: 'number', required: true },
      { name: 'shotsBlocked', label: 'Shots Blocked', type: 'number', required: true },
      { name: 'corners', label: 'Corners', type: 'number', required: true },
      { name: 'freeKicks', label: 'Free Kicks', type: 'number', required: true },
      { name: 'goalKicks', label: 'Goal Kicks', type: 'number', required: true },
      { name: 'throwIns', label: 'Throw Ins', type: 'number', required: true },
      { name: 'offsides', label: 'Offsides', type: 'number', required: true },
      { name: 'fouls', label: 'Fouls', type: 'number', required: true },
      { name: 'yellowCards', label: 'Yellow Cards', type: 'number', required: true },
      { name: 'yellowRedCards', label: 'Yellow-Red Cards', type: 'number', required: true },
      { name: 'redCards', label: 'Red Cards', type: 'number', required: true },
      { name: 'passes', label: 'Passes', type: 'number', required: true },
      { name: 'passesAccurate', label: 'Accurate Passes', type: 'number', required: true },
      { name: 'tackles', label: 'Tackles', type: 'number', required: true },
      { name: 'interceptions', label: 'Interceptions', type: 'number', required: true },
      { name: 'clearances', label: 'Clearances', type: 'number', required: true },
      { name: 'saves', label: 'Saves', type: 'number', required: true },
      { name: 'xg', label: 'Expected Goals (xG)', type: 'number', step: 0.001 },
      { name: 'xga', label: 'Expected Goals Against (xGA)', type: 'number', step: 0.001 }
    ]
  },
  matchstatisticsplayers: {
    title: 'Match Statistics (Player)', resource: 'matchstatisticsplayers', idField: 'id', itemName: 'player stat line',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Player', field: 'player.fullName' }, { header: 'Min', field: 'minutesPlayed', type: 'number' }, { header: 'Goals', field: 'goals', type: 'number' }, { header: 'Rating', field: 'rating', type: 'number' }],
    formFields: [
      fk('matchId', 'Match', 'matches'),
      fk('teamId', 'Team', 'teams'),
      fk('playerId', 'Player', 'players', 'fullName'),
      { name: 'minutesPlayed', label: 'Minutes Played', type: 'number', required: true },
      { name: 'goals', label: 'Goals', type: 'number', required: true },
      { name: 'assists', label: 'Assists', type: 'number', required: true },
      { name: 'shots', label: 'Shots', type: 'number', required: true },
      { name: 'shotsOnTarget', label: 'Shots on Target', type: 'number', required: true },
      { name: 'xg', label: 'Expected Goals (xG)', type: 'number', step: 0.001 },
      { name: 'keyPasses', label: 'Key Passes', type: 'number', required: true },
      { name: 'passes', label: 'Passes', type: 'number', required: true },
      { name: 'passesAccurate', label: 'Accurate Passes', type: 'number', required: true },
      { name: 'longBalls', label: 'Long Balls', type: 'number', required: true },
      { name: 'crosses', label: 'Crosses', type: 'number', required: true },
      { name: 'dribblesAttempted', label: 'Dribbles Attempted', type: 'number', required: true },
      { name: 'dribblesSucceeded', label: 'Dribbles Succeeded', type: 'number', required: true },
      { name: 'tackles', label: 'Tackles', type: 'number', required: true },
      { name: 'interceptions', label: 'Interceptions', type: 'number', required: true },
      { name: 'clearances', label: 'Clearances', type: 'number', required: true },
      { name: 'foulsCommitted', label: 'Fouls Committed', type: 'number', required: true },
      { name: 'foulsDrawn', label: 'Fouls Drawn', type: 'number', required: true },
      { name: 'yellowCards', label: 'Yellow Cards', type: 'number', required: true },
      { name: 'redCards', label: 'Red Cards', type: 'number', required: true },
      { name: 'saves', label: 'Saves', type: 'number', required: true },
      { name: 'goalsConceded', label: 'Goals Conceded', type: 'number', required: true },
      { name: 'rating', label: 'Rating', type: 'number', step: 0.01 }
    ]
  },
  standings: {
    title: 'Standings', resource: 'standings', idField: 'id', itemName: 'standing',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Position', field: 'rankPosition', type: 'number' }, { header: 'Team', field: 'team.name' }, { header: 'P', field: 'played', type: 'number' }, { header: 'Pts', field: 'points', type: 'number' }, { header: 'W', field: 'wins', type: 'number' }, { header: 'D', field: 'draws', type: 'number' }, { header: 'L', field: 'losses', type: 'number' }],
    formFields: [
      fk('seasonId', 'Season', 'seasons'),
      fk('stageId', 'Stage', 'stages'),
      fk('groupId', 'Group', 'groups'),
      fk('teamId', 'Team', 'teams'),
      { name: 'rankPosition', label: 'Position', type: 'number', required: true },
      { name: 'played', label: 'Played', type: 'number', required: true },
      { name: 'wins', label: 'Wins', type: 'number', required: true },
      { name: 'draws', label: 'Draws', type: 'number', required: true },
      { name: 'losses', label: 'Losses', type: 'number', required: true },
      { name: 'homeWins', label: 'Home Wins', type: 'number' },
      { name: 'homeDraws', label: 'Home Draws', type: 'number' },
      { name: 'homeLosses', label: 'Home Losses', type: 'number' },
      { name: 'awayWins', label: 'Away Wins', type: 'number' },
      { name: 'awayDraws', label: 'Away Draws', type: 'number' },
      { name: 'awayLosses', label: 'Away Losses', type: 'number' },
      { name: 'goalsFor', label: 'Goals For', type: 'number', required: true },
      { name: 'goalsAgainst', label: 'Goals Against', type: 'number', required: true },
      { name: 'goalDifference', label: 'Goal Difference', type: 'number' },
      { name: 'points', label: 'Points', type: 'number', required: true },
      { name: 'form', label: 'Form', type: 'text', maxLength: 255 }
    ]
  },
  injurys: {
    title: 'Injuries', resource: 'injurys', idField: 'id', itemName: 'injury',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Player', field: 'player.fullName' }, { header: 'Team', field: 'team.name' }, { header: 'Type', field: 'injuryType' }, { header: 'Severity', field: 'severity' }, { header: 'Status', field: 'status' }],
    formFields: [
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('teamId', 'Team', 'teams'),
      fk('matchId', 'Match', 'matches'),
      { name: 'injuryType', label: 'Injury Type', type: 'text', required: true, maxLength: 100 },
      { name: 'bodyPart', label: 'Body Part', type: 'text', maxLength: 100 },
      enumSelect('Severity', 'severity', INJURY_SEVERITIES),
      { name: 'startDate', label: 'Start Date', type: 'date', required: true },
      { name: 'expectedReturn', label: 'Expected Return', type: 'date' },
      { name: 'actualReturn', label: 'Actual Return', type: 'date' },
      enumSelect('Status', 'status', INJURY_STATUSES)
    ]
  },
  suspensions: {
    title: 'Suspensions', resource: 'suspensions', idField: 'id', itemName: 'suspension',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Player', field: 'player.fullName' }, { header: 'Team', field: 'team.name' }, { header: 'Card Type', field: 'cardType' }, { header: 'Banned', field: 'matchesBanned', type: 'number' }, { header: 'Status', field: 'status' }],
    formFields: [
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('teamId', 'Team', 'teams'),
      fk('competitionId', 'Competition', 'competitions'),
      fk('seasonId', 'Season', 'seasons'),
      enumSelect('Card Type', 'cardType', SUSPENSION_CARD_TYPES),
      { name: 'reason', label: 'Reason', type: 'textarea', maxLength: 255 },
      { name: 'startDate', label: 'Start Date', type: 'date', required: true },
      { name: 'endDate', label: 'End Date', type: 'date' },
      { name: 'matchesBanned', label: 'Matches Banned', type: 'number', required: true },
      { name: 'matchesRemaining', label: 'Matches Remaining', type: 'number', required: true },
      enumSelect('Status', 'status', SUSPENSION_STATUSES)
    ]
  },
  trophies: {
    title: 'Trophies', resource: 'trophies', idField: 'id', itemName: 'trophy',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Type', field: 'type' }, { header: 'Competition', field: 'competition.name' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      enumSelect('Type', 'type', TROPHY_TYPES),
      fk('competitionId', 'Competition', 'competitions'),
      { name: 'logo', label: 'Logo', type: 'file', fileKind: 'image' }
    ]
  },
  teamtrophys: {
    title: 'Team Trophies', resource: 'teamtrophys', idField: 'id', itemName: 'team trophy',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Team', field: 'team.name' }, { header: 'Trophy', field: 'trophy.name' }, { header: 'Season', field: 'season.name' }],
    formFields: [
      fk('teamId', 'Team', 'teams'),
      fk('trophyId', 'Trophy', 'trophies'),
      fk('seasonId', 'Season', 'seasons')
    ]
  },
  playerawards: {
    title: 'Player Awards', resource: 'playerawards', idField: 'id', itemName: 'player award',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Player', field: 'player.fullName' }, { header: 'Trophy', field: 'trophy.name' }, { header: 'Season', field: 'season.name' }],
    formFields: [
      fk('playerId', 'Player', 'players', 'fullName'),
      fk('trophyId', 'Trophy', 'trophies'),
      fk('seasonId', 'Season', 'seasons'),
      fk('teamId', 'Team', 'teams')
    ]
  },
  'head-to-head': {
    title: 'Head to Head', resource: 'head-to-head', idField: 'id', itemName: 'H2H record',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Team 1', field: 'team1.name' }, { header: 'Team 2', field: 'team2.name' }, { header: 'Total', field: 'totalMatches', type: 'number' }],
    formFields: [
      fk('team1Id', 'Team 1', 'teams'),
      fk('team2Id', 'Team 2', 'teams'),
      { name: 'totalMatches', label: 'Total Matches', type: 'number', required: true },
      { name: 'team1Wins', label: 'Team 1 Wins', type: 'number' },
      { name: 'team2Wins', label: 'Team 2 Wins', type: 'number' },
      { name: 'draws', label: 'Draws', type: 'number' },
      { name: 'team1Goals', label: 'Team 1 Goals', type: 'number' },
      { name: 'team2Goals', label: 'Team 2 Goals', type: 'number' }
    ]
  },
  bookmakers: {
    title: 'Bookmakers', resource: 'bookmakers', idField: 'id', itemName: 'bookmaker',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'name' }, { header: 'Website', field: 'website' }, { header: 'Active', field: 'active', type: 'boolean' }],
    formFields: [
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 100 },
      { name: 'website', label: 'Website', type: 'url' },
      { name: 'logo', label: 'Logo', type: 'file', fileKind: 'image' },
      boolSelect('Active', 'active')
    ]
  },
  odds: {
    title: 'Odds', resource: 'odds', idField: 'id', itemName: 'odd',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Market', field: 'market' }, { header: 'Selection', field: 'selection' }, { header: 'Odd', field: 'odd', type: 'number' }, { header: 'Bookmaker', field: 'bookmaker.name' }],
    formFields: [
      fk('bookmakerId', 'Bookmaker', 'bookmakers'),
      fk('matchId', 'Match', 'matches'),
      { name: 'market', label: 'Market', type: 'text', required: true, maxLength: 100 },
      { name: 'selection', label: 'Selection', type: 'text', required: true, maxLength: 100 },
      { name: 'odd', label: 'Odd', type: 'number', required: true, step: 0.001 },
      boolSelect('Active', 'active'),
      { name: 'recordedAt', label: 'Recorded At', type: 'datetime-local', required: true }
    ]
  },
  oddhistorys: {
    title: 'Odds History', resource: 'oddhistorys', idField: 'id', itemName: 'odd history entry',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Bookmaker', field: 'bookmaker.name' }, { header: 'Match', field: 'match.kickoff', type: 'date' }, { header: 'Market', field: 'market' }, { header: 'Odd', field: 'odd', type: 'number' }, { header: 'Recorded', field: 'recordedAt', type: 'date' }],
    formFields: [
      fk('bookmakerId', 'Bookmaker', 'bookmakers'),
      fk('matchId', 'Match', 'matches'),
      { name: 'market', label: 'Market', type: 'text', required: true, maxLength: 100 },
      { name: 'selection', label: 'Selection', type: 'text', required: true, maxLength: 100 },
      { name: 'odd', label: 'Odd', type: 'number', required: true, step: 0.001 },
      { name: 'recordedAt', label: 'Recorded At', type: 'datetime-local', required: true }
    ]
  },
  news: {
    title: 'News', resource: 'news', idField: 'id', itemName: 'news article',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Title', field: 'title' }, { header: 'Language', field: 'language' }, { header: 'Published', field: 'publishedAt', type: 'date' }],
    formFields: [
      { name: 'title', label: 'Title', type: 'text', required: true, maxLength: 500 },
      { name: 'slug', label: 'Slug', type: 'text', required: true, maxLength: 500 },
      { name: 'content', label: 'Content', type: 'textarea', required: true },
      { name: 'excerpt', label: 'Excerpt', type: 'textarea' },
      fk('competitionId', 'Competition', 'competitions'),
      fk('teamId', 'Team', 'teams'),
      fk('playerId', 'Player', 'players', 'fullName'),
      { name: 'image', label: 'Image', type: 'file', fileKind: 'image' },
      { name: 'author', label: 'Author', type: 'text', maxLength: 200 },
      { name: 'language', label: 'Language', type: 'text', required: true, maxLength: 10 },
      { name: 'sourceUrl', label: 'Source URL', type: 'url' },
      { name: 'publishedAt', label: 'Published At', type: 'datetime-local', required: true }
    ]
  },
  medias: {
    title: 'Media', resource: 'medias', idField: 'id', itemName: 'media item',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Entity', field: 'entityType' }, { header: 'Type', field: 'mediaType' }, { header: 'Title', field: 'title' }],
    formFields: [
      enumSelect('Entity Type', 'entityType', MEDIA_ENTITY_TYPES),
      { name: 'entityId', label: 'Entity ID', type: 'text', required: true },
      enumSelect('Media Type', 'mediaType', MEDIA_TYPES),
      { name: 'title', label: 'Title', type: 'text', required: true, maxLength: 300 },
      { name: 'description', label: 'Description', type: 'textarea' },
      { name: 'url', label: 'URL', type: 'url', required: true },
      { name: 'thumbnailUrl', label: 'Thumbnail URL', type: 'url' },
      { name: 'duration', label: 'Duration (seconds)', type: 'number' },
      { name: 'language', label: 'Language', type: 'text', maxLength: 10 },
      { name: 'publishedAt', label: 'Published At', type: 'datetime-local', required: true }
    ]
  },
  sponsors: {
    title: 'Sponsors', resource: 'sponsors', idField: 'id', itemName: 'sponsor',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Name', field: 'nom' }, { header: 'Website', field: 'websiteUrl' }, { header: 'Rating', field: 'rating', type: 'number' }],
    formFields: [
      { name: 'nom', label: 'Name', type: 'text', required: true, maxLength: 200 },
      { name: 'imageUrl', label: 'Image', type: 'file', fileKind: 'image', required: true },
      { name: 'description', label: 'Description', type: 'textarea' },
      { name: 'websiteUrl', label: 'Website', type: 'url' },
      { name: 'rating', label: 'Rating', type: 'number', step: 0.1 }
    ]
  },
  sponsorlinks: {
    title: 'Sponsor Links', resource: 'sponsorlinks', idField: 'id', itemName: 'sponsor link',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Sponsor', field: 'sponsor.nom' }, { header: 'Entity', field: 'entityType' }, { header: 'Start', field: 'dateDebut', type: 'date' }, { header: 'End', field: 'dateFin', type: 'date' }],
    formFields: [
      fk('sponsorId', 'Sponsor', 'sponsors', 'nom'),
      enumSelect('Entity Type', 'entityType', SPONSOR_ENTITY_TYPES),
      { name: 'entityId', label: 'Entity ID', type: 'text', required: true },
      { name: 'dateDebut', label: 'Start Date', type: 'date' },
      { name: 'dateFin', label: 'End Date', type: 'date' }
    ]
  },
  translations: {
    title: 'Translations', resource: 'translations', idField: 'id', itemName: 'translation',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Entity', field: 'entityType' }, { header: 'Language', field: 'language' }, { header: 'Field', field: 'fieldName' }, { header: 'Value', field: 'translatedValue' }],
    formFields: [
      { name: 'entityType', label: 'Entity Type', type: 'text', required: true },
      { name: 'entityId', label: 'Entity ID', type: 'text', required: true },
      { name: 'language', label: 'Language', type: 'text', required: true, maxLength: 10 },
      { name: 'fieldName', label: 'Field Name', type: 'text', required: true, maxLength: 100 },
      { name: 'translatedValue', label: 'Translated Value', type: 'textarea', required: true }
    ]
  },
  'api-users': {
    title: 'API Users', resource: 'api-users', idField: 'id', itemName: 'API user',
    columns: [{ header: 'ID', field: 'id' }, { header: 'Email', field: 'email' }, { header: 'Name', field: 'name' }, { header: 'Plan', field: 'plan' }, { header: 'Active', field: 'active', type: 'boolean' }],
    formFields: [
      { name: 'email', label: 'Email', type: 'email', required: true },
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      enumSelect('Plan', 'plan', API_PLANS),
      boolSelect('Active', 'active')
    ]
  },
  'api-keys': {
    title: 'API Keys', resource: 'api-keys', idField: 'id', itemName: 'API key',
    columns: [{ header: 'ID', field: 'id' }, { header: 'User', field: 'user.email' }, { header: 'Name', field: 'name' }, { header: 'Active', field: 'active', type: 'boolean' }],
    formFields: [
      fk('userId', 'User', 'api-users', 'email'),
      { name: 'keyHash', label: 'Key Hash', type: 'text', required: true, maxLength: 255 },
      { name: 'name', label: 'Name', type: 'text', required: true, maxLength: 200 },
      boolSelect('Active', 'active'),
      { name: 'expiresAt', label: 'Expires At', type: 'datetime-local' }
    ]
  },
  ratelimits: {
    title: 'Rate Limits', resource: 'ratelimits', idField: 'id', itemName: 'rate limit',
    columns: [{ header: 'ID', field: 'id' }, { header: 'API Key', field: 'apiKey.name' }, { header: 'Per Min', field: 'requestsPerMinute', type: 'number' }, { header: 'Per Day', field: 'requestsPerDay', type: 'number' }, { header: 'Per Month', field: 'requestsPerMonth', type: 'number' }],
    formFields: [
      fk('apiKeyId', 'API Key', 'api-keys', 'name'),
      { name: 'requestsPerMinute', label: 'Requests Per Minute', type: 'number', required: true },
      { name: 'requestsPerDay', label: 'Requests Per Day', type: 'number', required: true },
      { name: 'requestsPerMonth', label: 'Requests Per Month', type: 'number', required: true }
    ]
  },
  'audit-logs': {
    title: 'Audit Logs', resource: 'audit-logs', idField: 'id', itemName: 'audit log',
    columns: [{ header: 'ID', field: 'id' }, { header: 'API Key', field: 'apiKey.name' }, { header: 'Endpoint', field: 'endpoint' }, { header: 'Method', field: 'method' }, { header: 'Status', field: 'statusCode', type: 'number' }, { header: 'Time (ms)', field: 'responseTimeMs', type: 'number' }],
    formFields: [
      fk('apiKeyId', 'API Key', 'api-keys', 'name'),
      { name: 'endpoint', label: 'Endpoint', type: 'text', required: true },
      enumSelect('Method', 'method', HTTP_METHODS),
      { name: 'statusCode', label: 'Status Code', type: 'number', required: true },
      { name: 'responseTimeMs', label: 'Response Time (ms)', type: 'number' },
      { name: 'ipAddress', label: 'IP Address', type: 'text' }
    ]
  }
};

export function getEntityConfig(resource: string): EntityConfig | undefined {
  return ENTITY_CONFIGS[resource];
}

export const ENTITY_FILTERS: Record<string, FilterDef[]> = {
  continents: [],
  countries: [fk('continentId', 'Continent', 'continents')],
  cities: [fk('countryId', 'Country', 'countries')],
  confederations: [fk('continentId', 'Continent', 'continents')],
  competitions: [
    enumSelect('Type', 'type', COMPETITION_TYPES, false),
    enumSelect('Gender', 'gender', GENDERS, false),
    enumSelect('Age Level', 'ageLevel', AGE_LEVELS, false),
    enumSelect('Sport', 'sport', SPORTS, false),
    boolSelect('Active', 'active')
  ],
  seasons: [enumSelect('Status', 'status', SEASON_STATUSES, false)],
  stages: [enumSelect('Type', 'type', STAGE_TYPES, false)],
  groups: [fk('stageId', 'Stage', 'stages')],
  groupteams: [boolSelect('Eliminated', 'eliminated'), boolSelect('Qualified', 'qualified')],
  teamseasonparticipations: [
    enumSelect('Entry Type', 'entryType', ENTRY_TYPES, false),
    enumSelect('Outcome', 'outcome', SEASON_OUTCOMES, false),
    boolSelect('Withdrawn', 'withdrawn')
  ],
  rounds: [
    enumSelect('Type', 'type', ROUND_TYPES, false),
    enumSelect('Status', 'status', SEASON_STATUSES, false),
    fk('stageId', 'Stage', 'stages')
  ],
  teams: [
    enumSelect('Type', 'type', TEAM_TYPES, false),
    boolSelect('National Team', 'nationalTeam'),
    boolSelect('Active', 'active'),
    fk('countryId', 'Country', 'countries')
  ],
  stadiums: [
    enumSelect('Surface', 'surface', SURFACES, false),
    fk('countryId', 'Country', 'countries'),
    fk('cityId', 'City', 'cities')
  ],
  referees: [
    enumSelect('Category', 'category', REFEREE_CATEGORIES, false),
    boolSelect('Active', 'active'),
    fk('countryId', 'Country', 'countries')
  ],
  coaches: [
    enumSelect('Role', 'role', COACH_ROLES, false),
    boolSelect('Active', 'active'),
    fk('countryId', 'Country', 'countries')
  ],
  teamcoachs: [enumSelect('Role', 'role', COACH_ROLES, false), boolSelect('Interim', 'interim')],
  players: [
    enumSelect('Preferred Foot', 'preferredFoot', FOOT_PREFERENCES, false),
    enumSelect('Position', 'position', PLAYER_POSITIONS, false),
    enumSelect('Status', 'status', PLAYER_STATUSES, false),
    fk('countryId', 'Country', 'countries'),
    fk('nationalityId', 'Nationality', 'countries'),
    fk('secondNationalityId', '2nd Nationality', 'countries')
  ],
  playerseasonregistrations: [
    enumSelect('Position', 'position', PLAYER_POSITIONS, false),
    enumSelect('Status', 'status', REGISTRATION_STATUSES, false),
    boolSelect('Captain', 'captain'),
    fk('teamId', 'Team', 'teams')
  ],
  playerseasonstats: [fk('teamId', 'Team', 'teams')],
  contracts: [boolSelect('Current', 'current')],
  transfers: [enumSelect('Transfer Type', 'transferType', TRANSFER_TYPES, false)],
  matches: [enumSelect('Status', 'status', MATCH_STATUSES, false), enumSelect('Period', 'period', MATCH_PERIODS, false)],
  lineups: [boolSelect('Starter', 'starter'), boolSelect('Captain', 'captain')],
  matchformations: [],
  matchevents: [
    enumSelect('Period', 'period', MATCH_PERIODS, false),
    enumSelect('Event Type', 'eventType', EVENT_TYPES, false),
    boolSelect('VAR Reviewed', 'varReviewed')
  ],
  matchpenaltyshootoutshots: [enumSelect('Status', 'status', SHOOTOUT_STATUSES, false)],
  matchreferees: [enumSelect('Role', 'role', MATCH_REFEREE_ROLES, false)],
  matchstatisticsteams: [],
  matchstatisticsplayers: [],
  standings: [],
  injurys: [enumSelect('Severity', 'severity', INJURY_SEVERITIES, false), enumSelect('Status', 'status', INJURY_STATUSES, false)],
  suspensions: [enumSelect('Card Type', 'cardType', SUSPENSION_CARD_TYPES, false), enumSelect('Status', 'status', SUSPENSION_STATUSES, false)],
  trophies: [enumSelect('Type', 'type', TROPHY_TYPES, false)],
  teamtrophys: [],
  playerawards: [],
  'head-to-head': [],
  bookmakers: [boolSelect('Active', 'active')],
  odds: [boolSelect('Active', 'active')],
  oddhistorys: [],
  news: [],
  medias: [enumSelect('Entity Type', 'entityType', MEDIA_ENTITY_TYPES, false), enumSelect('Media Type', 'mediaType', MEDIA_TYPES, false)],
  sponsors: [],
  sponsorlinks: [enumSelect('Entity Type', 'entityType', SPONSOR_ENTITY_TYPES, false)],
  translations: [],
  'api-users': [enumSelect('Plan', 'plan', API_PLANS, false)],
  'api-keys': [boolSelect('Active', 'active')],
  ratelimits: [],
  'audit-logs': [enumSelect('Method', 'method', HTTP_METHODS, false)]
};

export const ENTITY_SEARCH_PLACEHOLDERS: Record<string, string> = {
  continents: 'Search by code or name...',
  countries: 'Search by name, ISO2, ISO3, FIFA code...',
  cities: 'Search by name or country...',
  confederations: 'Search by name, acronym or continent...',
  competitions: 'Search by name, short name, country or confederation...',
  seasons: 'Search by name or competition...',
  stages: 'Search by name or season...',
  groups: 'Search by name or stage...',
  groupteams: 'Search by group or team...',
  teamseasonparticipations: 'Search by team, season or entry type...',
  rounds: 'Search by name, slug or stage...',
  teams: 'Search by name, short name, code, country or stadium...',
  stadiums: 'Search by name, address, country or city...',
  referees: 'Search by full name or country...',
  coaches: 'Search by full name or country...',
  teamcoachs: 'Search by team or coach...',
  players: 'Search by full name, birth place or country...',
  playerseasonregistrations: 'Search by player, team or position...',
  playerseasonstats: 'Search by player or team...',
  contracts: 'Search by player or team...',
  transfers: 'Search by player or teams...',
  matches: 'Search by home/away team, status or period...',
  lineups: 'Search by player, team or position...',
  matchformations: 'Search by team or formation...',
  matchevents: 'Search by player, team or event type...',
  matchpenaltyshootoutshots: 'Search by player, team or status...',
  matchreferees: 'Search by referee...',
  matchstatisticsteams: 'Search by team...',
  matchstatisticsplayers: 'Search by player or team...',
  standings: 'Search by team or form...',
  injurys: 'Search by player, team or injury type...',
  suspensions: 'Search by player, team or reason...',
  trophies: 'Search by name or competition...',
  teamtrophys: 'Search by team or trophy...',
  playerawards: 'Search by player or trophy...',
  'head-to-head': 'Search by team...',
  bookmakers: 'Search by name...',
  odds: 'Search by market, selection or bookmaker...',
  oddhistorys: 'Search by market, selection or bookmaker...',
  news: 'Search by title, slug, author, team or player...',
  medias: 'Search by title, URL or entity type...',
  sponsors: 'Search by name or website...',
  sponsorlinks: 'Search by sponsor...',
  translations: 'Search by entity type, field, value or language...',
  'api-users': 'Search by name or email...',
  'api-keys': 'Search by name or user...',
  ratelimits: 'Search by API key...',
  'audit-logs': 'Search by endpoint, method or API key...'
};

export interface EntityScope {
  competition?: boolean;
  season?: boolean;
}

export const ENTITY_SCOPE: Record<string, EntityScope> = {
  seasons: { competition: true },
  stages: { season: true },
  matches: { season: true },
  standings: { season: true },
  teamcoachs: { season: true },
  transfers: { season: true },
  teamseasonparticipations: { season: true },
  playerseasonregistrations: { competition: true, season: true },
  playerseasonstats: { competition: true, season: true },
  suspensions: { competition: true, season: true },
  playerawards: { season: true },
  teamtrophys: { season: true },
  news: { competition: true },
  trophies: { competition: true }
};

export function getEntityScope(resource: string): EntityScope {
  return ENTITY_SCOPE[resource] || {};
}

export function getEntityFilters(resource: string): FilterDef[] {
  return ENTITY_FILTERS[resource] || [];
}

export function getEntitySearchPlaceholder(resource: string): string {
  return ENTITY_SEARCH_PLACEHOLDERS[resource] || `Search ${(ENTITY_CONFIGS[resource]?.title || resource).toLowerCase()}...`;
}

export const EXCLUDED_FORM_FIELDS = ['id'];
