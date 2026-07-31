CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS continents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS countries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    continent_id UUID NOT NULL REFERENCES continents(id),
    name VARCHAR(100) NOT NULL,
    official_name VARCHAR(200),
    iso2 CHAR(2) NOT NULL,
    iso3 CHAR(3) NOT NULL,
    fifa_code VARCHAR(3),
    flag_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cities (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL REFERENCES countries(id),
    name VARCHAR(200) NOT NULL,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    timezone VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS confederations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    continent_id UUID NOT NULL REFERENCES continents(id),
    name VARCHAR(200) NOT NULL,
    acronym VARCHAR(20) NOT NULL,
    logo TEXT,
    website TEXT,
    founded INTEGER,
    headquarters VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS competitions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id UUID REFERENCES countries(id),
    confederation_id UUID REFERENCES confederations(id),
    name VARCHAR(200) NOT NULL,
    short_name VARCHAR(100),
    type VARCHAR(50) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    age_level VARCHAR(20) NOT NULL,
    sport VARCHAR(50) NOT NULL,
    level INTEGER,
    logo TEXT,
    founded INTEGER,
    website TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS seasons (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    competition_id UUID NOT NULL REFERENCES competitions(id),
    name VARCHAR(100) NOT NULL,
    year_start INTEGER NOT NULL,
    year_end INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    current BOOLEAN NOT NULL DEFAULT false,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    season_id UUID NOT NULL REFERENCES seasons(id),
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    order_no INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS groups (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    stage_id UUID NOT NULL REFERENCES stages(id),
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rounds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    stage_id UUID NOT NULL REFERENCES stages(id),
    group_id UUID REFERENCES groups(id),
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(150) NOT NULL,
    number INTEGER NOT NULL,
    type VARCHAR(50) NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT false,
    status VARCHAR(20) NOT NULL,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS teams (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL REFERENCES countries(id),
    stadium_id UUID REFERENCES stadiums(id),
    type VARCHAR(50) NOT NULL,
    is_national_team BOOLEAN NOT NULL DEFAULT false,
    name VARCHAR(200) NOT NULL,
    short_name VARCHAR(100),
    code VARCHAR(20),
    founded INTEGER,
    logo TEXT,
    kit_primary_color VARCHAR(7),
    kit_secondary_color VARCHAR(7),
    website TEXT,
    address TEXT,
    phone VARCHAR(50),
    email VARCHAR(200),
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stadiums (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL REFERENCES countries(id),
    city_id UUID NOT NULL REFERENCES cities(id),
    name VARCHAR(200) NOT NULL,
    capacity INTEGER,
    surface VARCHAR(50),
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    address TEXT,
    opened INTEGER,
    image TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS referees (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL REFERENCES countries(id),
    first_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    birth_date DATE,
    photo TEXT,
    category VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS match_referees (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id UUID NOT NULL REFERENCES matches(id),
    referee_id UUID NOT NULL REFERENCES referees(id),
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coaches (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL REFERENCES countries(id),
    first_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    birth_date DATE,
    photo TEXT,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS team_coaches (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    team_id UUID NOT NULL REFERENCES teams(id),
    coach_id UUID NOT NULL REFERENCES coaches(id),
    season_id UUID NOT NULL REFERENCES seasons(id),
    role VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_interim BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS players (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL REFERENCES countries(id),
    nationality_id UUID NOT NULL REFERENCES countries(id),
    second_nationality_id UUID REFERENCES countries(id),
    first_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    birth_date DATE NOT NULL,
    birth_place VARCHAR(100),
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    preferred_foot VARCHAR(20),
    position VARCHAR(50) NOT NULL,
    photo TEXT,
    market_value DECIMAL(15,2),
    status VARCHAR(50) NOT NULL,
    twitter VARCHAR(200),
    instagram VARCHAR(200),
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS player_season_registrations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    season_id UUID NOT NULL REFERENCES seasons(id),
    competition_id UUID NOT NULL REFERENCES competitions(id),
    shirt_number INTEGER,
    position VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    registered_at DATE,
    unregistered_at DATE,
    is_captain BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS player_season_stats (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    season_id UUID NOT NULL REFERENCES seasons(id),
    competition_id UUID NOT NULL REFERENCES competitions(id),
    appearances INTEGER NOT NULL DEFAULT 0,
    appearances_as_starter INTEGER NOT NULL DEFAULT 0,
    minutes_played INTEGER NOT NULL DEFAULT 0,
    goals INTEGER NOT NULL DEFAULT 0,
    assists INTEGER NOT NULL DEFAULT 0,
    shots INTEGER NOT NULL DEFAULT 0,
    shots_on_target INTEGER NOT NULL DEFAULT 0,
    xg DECIMAL(8,3),
    key_passes INTEGER NOT NULL DEFAULT 0,
    passes INTEGER NOT NULL DEFAULT 0,
    passes_accurate INTEGER NOT NULL DEFAULT 0,
    dribbles_attempted INTEGER NOT NULL DEFAULT 0,
    dribbles_succeeded INTEGER NOT NULL DEFAULT 0,
    tackles INTEGER NOT NULL DEFAULT 0,
    interceptions INTEGER NOT NULL DEFAULT 0,
    fouls_committed INTEGER NOT NULL DEFAULT 0,
    fouls_drawn INTEGER NOT NULL DEFAULT 0,
    yellow_cards INTEGER NOT NULL DEFAULT 0,
    red_cards INTEGER NOT NULL DEFAULT 0,
    saves INTEGER NOT NULL DEFAULT 0,
    goals_conceded INTEGER NOT NULL DEFAULT 0,
    clean_sheets INTEGER NOT NULL DEFAULT 0,
    avg_rating DECIMAL(4,2),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS contracts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    salary NUMERIC(15,2),
    shirt_number INTEGER,
    is_current BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transfers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id),
    from_team_id UUID REFERENCES teams(id),
    to_team_id UUID NOT NULL REFERENCES teams(id),
    transfer_date DATE NOT NULL,
    fee NUMERIC(15,2),
    currency CHAR(3),
    transfer_type VARCHAR(50) NOT NULL,
    season_id UUID NOT NULL REFERENCES seasons(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS matches (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    season_id UUID NOT NULL REFERENCES seasons(id),
    stage_id UUID NOT NULL REFERENCES stages(id),
    group_id UUID REFERENCES groups(id),
    round_id UUID NOT NULL REFERENCES rounds(id),
    home_team_id UUID NOT NULL REFERENCES teams(id),
    away_team_id UUID NOT NULL REFERENCES teams(id),
    stadium_id UUID REFERENCES stadiums(id),
    referee_id UUID REFERENCES referees(id),
    kickoff TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    period VARCHAR(20),
    minute INTEGER,
    minute_extra INTEGER,
    first_half_start TIMESTAMP,
    second_half_start TIMESTAMP,
    extra_time_start TIMESTAMP,
    penalty_shootout_start TIMESTAMP,
    home_score INTEGER,
    away_score INTEGER,
    home_ht_score INTEGER,
    away_ht_score INTEGER,
    home_et_score INTEGER,
    away_et_score INTEGER,
    home_penalty_score INTEGER,
    away_penalty_score INTEGER,
    home_penalty_form VARCHAR(30),
    away_penalty_form VARCHAR(30),
    attendance INTEGER,
    weather VARCHAR(100),
    temperature DECIMAL(4,1),
    wind_speed DECIMAL(5,1),
    note TEXT,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lineups (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id UUID NOT NULL REFERENCES matches(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    player_id UUID NOT NULL REFERENCES players(id),
    starter BOOLEAN NOT NULL,
    captain BOOLEAN NOT NULL DEFAULT false,
    shirt_number INTEGER,
    position VARCHAR(30),
    position_x DECIMAL(5,2),
    position_y DECIMAL(5,2),
    formation_slot INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS match_formations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id UUID NOT NULL REFERENCES matches(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    formation VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS match_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id UUID NOT NULL REFERENCES matches(id),
    minute INTEGER NOT NULL,
    extra_minute INTEGER,
    period VARCHAR(20) NOT NULL,
    team_id UUID NOT NULL REFERENCES teams(id),
    player_id UUID REFERENCES players(id),
    related_player_id UUID REFERENCES players(id),
    event_type VARCHAR(50) NOT NULL,
    detail VARCHAR(100),
    comments TEXT,
    var_reviewed BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS match_penalty_shootout_shots (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id UUID NOT NULL REFERENCES matches(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    player_id UUID NOT NULL REFERENCES players(id),
    goalkeeper_id UUID NOT NULL REFERENCES players(id),
    shot_order INTEGER NOT NULL,
    round INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS match_statistics_team (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id UUID NOT NULL REFERENCES matches(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    possession DECIMAL(5,2) NOT NULL DEFAULT 0,
    shots INTEGER NOT NULL DEFAULT 0,
    shots_on_target INTEGER NOT NULL DEFAULT 0,
    shots_off_target INTEGER NOT NULL DEFAULT 0,
    shots_blocked INTEGER NOT NULL DEFAULT 0,
    corners INTEGER NOT NULL DEFAULT 0,
    free_kicks INTEGER NOT NULL DEFAULT 0,
    goal_kicks INTEGER NOT NULL DEFAULT 0,
    throw_ins INTEGER NOT NULL DEFAULT 0,
    offsides INTEGER NOT NULL DEFAULT 0,
    fouls INTEGER NOT NULL DEFAULT 0,
    yellow_cards INTEGER NOT NULL DEFAULT 0,
    yellow_red_cards INTEGER NOT NULL DEFAULT 0,
    red_cards INTEGER NOT NULL DEFAULT 0,
    passes INTEGER NOT NULL DEFAULT 0,
    passes_accurate INTEGER NOT NULL DEFAULT 0,
    tackles INTEGER NOT NULL DEFAULT 0,
    interceptions INTEGER NOT NULL DEFAULT 0,
    clearances INTEGER NOT NULL DEFAULT 0,
    saves INTEGER NOT NULL DEFAULT 0,
    xg DECIMAL(6,3),
    xga DECIMAL(6,3),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS match_statistics_player (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id UUID NOT NULL REFERENCES matches(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    player_id UUID NOT NULL REFERENCES players(id),
    minutes_played INTEGER NOT NULL DEFAULT 0,
    goals INTEGER NOT NULL DEFAULT 0,
    assists INTEGER NOT NULL DEFAULT 0,
    shots INTEGER NOT NULL DEFAULT 0,
    shots_on_target INTEGER NOT NULL DEFAULT 0,
    xg DECIMAL(6,3),
    key_passes INTEGER NOT NULL DEFAULT 0,
    passes INTEGER NOT NULL DEFAULT 0,
    passes_accurate INTEGER NOT NULL DEFAULT 0,
    long_balls INTEGER NOT NULL DEFAULT 0,
    crosses INTEGER NOT NULL DEFAULT 0,
    dribbles_attempted INTEGER NOT NULL DEFAULT 0,
    dribbles_succeeded INTEGER NOT NULL DEFAULT 0,
    tackles INTEGER NOT NULL DEFAULT 0,
    interceptions INTEGER NOT NULL DEFAULT 0,
    clearances INTEGER NOT NULL DEFAULT 0,
    fouls_committed INTEGER NOT NULL DEFAULT 0,
    fouls_drawn INTEGER NOT NULL DEFAULT 0,
    yellow_cards INTEGER NOT NULL DEFAULT 0,
    red_cards INTEGER NOT NULL DEFAULT 0,
    saves INTEGER NOT NULL DEFAULT 0,
    goals_conceded INTEGER NOT NULL DEFAULT 0,
    rating DECIMAL(4,2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS standings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    season_id UUID NOT NULL REFERENCES seasons(id),
    stage_id UUID NOT NULL REFERENCES stages(id),
    group_id UUID REFERENCES groups(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    rank_position INTEGER NOT NULL,
    played INTEGER NOT NULL DEFAULT 0,
    wins INTEGER NOT NULL DEFAULT 0,
    draws INTEGER NOT NULL DEFAULT 0,
    losses INTEGER NOT NULL DEFAULT 0,
    home_wins INTEGER NOT NULL DEFAULT 0,
    home_draws INTEGER NOT NULL DEFAULT 0,
    home_losses INTEGER NOT NULL DEFAULT 0,
    away_wins INTEGER NOT NULL DEFAULT 0,
    away_draws INTEGER NOT NULL DEFAULT 0,
    away_losses INTEGER NOT NULL DEFAULT 0,
    goals_for INTEGER NOT NULL DEFAULT 0,
    goals_against INTEGER NOT NULL DEFAULT 0,
    goal_difference INTEGER NOT NULL DEFAULT 0,
    points INTEGER NOT NULL DEFAULT 0,
    form VARCHAR(10),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS injuries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    match_id UUID REFERENCES matches(id),
    injury_type VARCHAR(100) NOT NULL,
    body_part VARCHAR(100),
    severity VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    expected_return DATE,
    actual_return DATE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS suspensions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    competition_id UUID NOT NULL REFERENCES competitions(id),
    season_id UUID NOT NULL REFERENCES seasons(id),
    card_type VARCHAR(50) NOT NULL,
    reason VARCHAR(255),
    start_date DATE NOT NULL,
    end_date DATE,
    matches_banned INTEGER NOT NULL DEFAULT 0,
    matches_remaining INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trophies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    competition_id UUID REFERENCES competitions(id),
    name VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL,
    logo TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS team_trophies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    team_id UUID NOT NULL REFERENCES teams(id),
    trophy_id UUID NOT NULL REFERENCES trophies(id),
    season_id UUID NOT NULL REFERENCES seasons(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS player_awards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id),
    trophy_id UUID NOT NULL REFERENCES trophies(id),
    season_id UUID NOT NULL REFERENCES seasons(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS head_to_head (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    team1_id UUID NOT NULL REFERENCES teams(id),
    team2_id UUID NOT NULL REFERENCES teams(id),
    total_matches INTEGER NOT NULL DEFAULT 0,
    team1_wins INTEGER NOT NULL DEFAULT 0,
    team2_wins INTEGER NOT NULL DEFAULT 0,
    draws INTEGER NOT NULL DEFAULT 0,
    team1_goals INTEGER NOT NULL DEFAULT 0,
    team2_goals INTEGER NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookmakers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    website TEXT,
    logo TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS odds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    bookmaker_id UUID NOT NULL REFERENCES bookmakers(id),
    match_id UUID NOT NULL REFERENCES matches(id),
    market VARCHAR(100) NOT NULL,
    selection VARCHAR(100) NOT NULL,
    odd DECIMAL(8,3) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS odds_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    bookmaker_id UUID NOT NULL REFERENCES bookmakers(id),
    match_id UUID NOT NULL REFERENCES matches(id),
    market VARCHAR(100) NOT NULL,
    selection VARCHAR(100) NOT NULL,
    odd DECIMAL(8,3) NOT NULL,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS news (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    competition_id UUID REFERENCES competitions(id),
    team_id UUID REFERENCES teams(id),
    player_id UUID REFERENCES players(id),
    title VARCHAR(500) NOT NULL,
    slug VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    excerpt TEXT,
    image TEXT,
    author VARCHAR(200),
    language VARCHAR(10) NOT NULL,
    source_url TEXT,
    published_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS media (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID NOT NULL,
    media_type VARCHAR(50) NOT NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT,
    url TEXT NOT NULL,
    thumbnail_url TEXT,
    duration INTEGER,
    language VARCHAR(10),
    published_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sponsors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(200) NOT NULL,
    image_url TEXT NOT NULL,
    description TEXT,
    website_url TEXT,
    rating DECIMAL(3,1),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sponsor_links (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sponsor_id UUID NOT NULL REFERENCES sponsors(id),
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    date_debut DATE,
    date_fin DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS translations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID NOT NULL,
    language VARCHAR(10) NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    translated_value VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS api_users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(300) NOT NULL,
    name VARCHAR(200) NOT NULL,
    plan VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS api_keys (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES api_users(id),
    key_hash VARCHAR(255) NOT NULL,
    name VARCHAR(200) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    expires_at TIMESTAMP,
    last_used_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rate_limits (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    api_key_id UUID NOT NULL REFERENCES api_keys(id),
    requests_per_minute INTEGER NOT NULL,
    requests_per_day INTEGER NOT NULL,
    requests_per_month INTEGER NOT NULL,
    current_minute_count INTEGER NOT NULL DEFAULT 0,
    current_day_count INTEGER NOT NULL DEFAULT 0,
    current_month_count INTEGER NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    api_key_id UUID NOT NULL REFERENCES api_keys(id),
    endpoint VARCHAR(500) NOT NULL,
    method VARCHAR(10) NOT NULL,
    status_code INTEGER NOT NULL,
    response_time_ms INTEGER NOT NULL,
    ip_address INET NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_continent_code ON continents(code);
CREATE INDEX IF NOT EXISTS idx_country_name ON countries(name);
CREATE INDEX IF NOT EXISTS idx_country_fifa ON countries(fifa_code);
CREATE INDEX IF NOT EXISTS idx_city_country ON cities(country_id);
CREATE INDEX IF NOT EXISTS idx_city_name ON cities(name);
CREATE INDEX IF NOT EXISTS idx_confederation_acronym ON confederations(acronym);
CREATE INDEX IF NOT EXISTS idx_competition_country ON competitions(country_id);
CREATE INDEX IF NOT EXISTS idx_competition_type ON competitions(type);
CREATE INDEX IF NOT EXISTS idx_competition_gender ON competitions(gender);
CREATE INDEX IF NOT EXISTS idx_competition_sport ON competitions(sport);
CREATE INDEX IF NOT EXISTS idx_season_competition ON seasons(competition_id);
CREATE INDEX IF NOT EXISTS idx_season_current ON seasons(current);
CREATE INDEX IF NOT EXISTS idx_round_stage ON rounds(stage_id);
CREATE INDEX IF NOT EXISTS idx_round_is_current ON rounds(is_current);
CREATE INDEX IF NOT EXISTS idx_round_status ON rounds(status);
CREATE INDEX IF NOT EXISTS idx_group_teams_group ON group_teams(group_id);
CREATE INDEX IF NOT EXISTS idx_group_teams_team ON group_teams(team_id);
CREATE INDEX IF NOT EXISTS idx_team_season_season ON team_season_participations(season_id);
CREATE INDEX IF NOT EXISTS idx_team_season_team ON team_season_participations(team_id);
CREATE INDEX IF NOT EXISTS idx_team_country ON teams(country_id);
CREATE INDEX IF NOT EXISTS idx_team_name ON teams(name);
CREATE INDEX IF NOT EXISTS idx_team_type ON teams(type);
CREATE INDEX IF NOT EXISTS idx_stadium_city ON stadiums(city_id);
CREATE INDEX IF NOT EXISTS idx_stadium_country ON stadiums(country_id);
CREATE INDEX IF NOT EXISTS idx_referee_country ON referees(country_id);
CREATE INDEX IF NOT EXISTS idx_referee_name ON referees(last_name);
CREATE INDEX IF NOT EXISTS idx_match_referees_match ON match_referees(match_id);
CREATE INDEX IF NOT EXISTS idx_match_referees_referee ON match_referees(referee_id);
CREATE INDEX IF NOT EXISTS idx_team_coaches_team ON team_coaches(team_id);
CREATE INDEX IF NOT EXISTS idx_team_coaches_coach ON team_coaches(coach_id);
CREATE INDEX IF NOT EXISTS idx_team_coaches_season ON team_coaches(season_id);
CREATE INDEX IF NOT EXISTS idx_player_country ON players(country_id);
CREATE INDEX IF NOT EXISTS idx_player_name ON players(last_name);
CREATE INDEX IF NOT EXISTS idx_player_position ON players(position);
CREATE INDEX IF NOT EXISTS idx_player_status ON players(status);
CREATE INDEX IF NOT EXISTS idx_player_season_reg_player ON player_season_registrations(player_id);
CREATE INDEX IF NOT EXISTS idx_player_season_reg_team ON player_season_registrations(team_id);
CREATE INDEX IF NOT EXISTS idx_player_season_reg_season ON player_season_registrations(season_id);
CREATE INDEX IF NOT EXISTS idx_player_season_stats_player ON player_season_stats(player_id);
CREATE INDEX IF NOT EXISTS idx_player_season_stats_season ON player_season_stats(season_id);
CREATE INDEX IF NOT EXISTS idx_player_season_stats_competition ON player_season_stats(competition_id);
CREATE INDEX IF NOT EXISTS idx_contract_player ON contracts(player_id);
CREATE INDEX IF NOT EXISTS idx_contract_team ON contracts(team_id);
CREATE INDEX IF NOT EXISTS idx_transfer_player ON transfers(player_id);
CREATE INDEX IF NOT EXISTS idx_match_kickoff ON matches(kickoff);
CREATE INDEX IF NOT EXISTS idx_match_status ON matches(status);
CREATE INDEX IF NOT EXISTS idx_match_home ON matches(home_team_id);
CREATE INDEX IF NOT EXISTS idx_match_away ON matches(away_team_id);
CREATE INDEX IF NOT EXISTS idx_match_season ON matches(season_id);
CREATE INDEX IF NOT EXISTS idx_match_stage ON matches(stage_id);
CREATE INDEX IF NOT EXISTS idx_event_match ON match_events(match_id);
CREATE INDEX IF NOT EXISTS idx_event_type ON match_events(event_type);
CREATE INDEX IF NOT EXISTS idx_event_minute ON match_events(minute);
CREATE INDEX IF NOT EXISTS idx_standings_season ON standings(season_id);
CREATE INDEX IF NOT EXISTS idx_standings_group ON standings(group_id);
CREATE INDEX IF NOT EXISTS idx_injury_player ON injuries(player_id);
CREATE INDEX IF NOT EXISTS idx_injury_status ON injuries(status);
CREATE INDEX IF NOT EXISTS idx_suspension_player ON suspensions(player_id);
CREATE INDEX IF NOT EXISTS idx_suspension_competition ON suspensions(competition_id);
CREATE INDEX IF NOT EXISTS idx_odds_match ON odds(match_id);
CREATE INDEX IF NOT EXISTS idx_odds_bookmaker ON odds(bookmaker_id);
CREATE INDEX IF NOT EXISTS idx_odds_history_match ON odds_history(match_id);
CREATE INDEX IF NOT EXISTS idx_odds_history_recorded_at ON odds_history(recorded_at);
CREATE INDEX IF NOT EXISTS idx_news_published_at ON news(published_at);
CREATE INDEX IF NOT EXISTS idx_news_language ON news(language);
CREATE INDEX IF NOT EXISTS idx_sponsor_nom ON sponsors(nom);
CREATE INDEX IF NOT EXISTS idx_sponsor_link_sponsor ON sponsor_links(sponsor_id);
CREATE INDEX IF NOT EXISTS idx_sponsor_link_entity ON sponsor_links(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_translation_entity ON translations(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_translation_language ON translations(language);
CREATE INDEX IF NOT EXISTS idx_audit_api_key ON audit_logs(api_key_id);
CREATE INDEX IF NOT EXISTS idx_audit_created_at ON audit_logs(created_at);

CREATE UNIQUE INDEX IF NOT EXISTS uq_continent_code ON continents(code);
CREATE UNIQUE INDEX IF NOT EXISTS uq_country_iso2 ON countries(iso2);
CREATE UNIQUE INDEX IF NOT EXISTS uq_country_iso3 ON countries(iso3);
CREATE UNIQUE INDEX IF NOT EXISTS uq_country_fifa ON countries(fifa_code);
CREATE UNIQUE INDEX IF NOT EXISTS uq_confederation_acronym ON confederations(acronym);
CREATE UNIQUE INDEX IF NOT EXISTS uq_group_group ON group_teams(group_id, team_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_team_season ON team_season_participations(season_id, team_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_round_stage_number ON rounds(stage_id, number);
CREATE UNIQUE INDEX IF NOT EXISTS uq_player_season_reg ON player_season_registrations(player_id, team_id, season_id, competition_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_player_season_stats ON player_season_stats(player_id, team_id, season_id, competition_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_match_team ON match_statistics_team(match_id, team_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_match_player ON match_statistics_player(match_id, player_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_team_trophies ON team_trophies(team_id, trophy_id, season_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_player_awards ON player_awards(player_id, trophy_id, season_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_head_to_head ON head_to_head(team1_id, team2_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_news_slug_language ON news(slug, language);
CREATE UNIQUE INDEX IF NOT EXISTS uq_sponsor_link ON sponsor_links(sponsor_id, entity_type, entity_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_translation ON translations(entity_type, entity_id, language, field_name);
CREATE UNIQUE INDEX IF NOT EXISTS uq_api_user_email ON api_users(email);
CREATE UNIQUE INDEX IF NOT EXISTS uq_api_key_hash ON api_keys(key_hash);
CREATE UNIQUE INDEX IF NOT EXISTS uq_rate_limit ON rate_limits(api_key_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_match_referee_role ON match_referees(match_id, referee_id, role);
CREATE UNIQUE INDEX IF NOT EXISTS uq_lineup ON lineups(match_id, team_id, player_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_match_formation ON match_formations(match_id, team_id);