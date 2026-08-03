# Architecture Complète - API Football

## Objectif

Base de données conçue pour gérer :
- Championnats
- Coupes
- Super Coupes
- Compétitions internationales
- Football féminin
- U17/U20/U23
- Futsal
- Beach Soccer
- Statistiques avancées
- Live Scores
- Bookmakers
- Actualités
- Médias
- Sponsors

---

# Module Géographie

## continents

Description : Liste des continents.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| code | VARCHAR(10) | Non | Non | Non | Code continent (EU, AF, AS...) |
| name | VARCHAR(100) | Non | Non | Non | Nom |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_continent_code

---

## countries

Description : Pays FIFA et non FIFA.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| continent_id | UUID | Non | continents.id | Non | Continent |
| name | VARCHAR(100) | Non | Non | Non | Nom |
| official_name | VARCHAR(200) | Non | Non | Oui | Nom officiel |
| iso2 | CHAR(2) | Non | Non | Non | ISO2 |
| iso3 | CHAR(3) | Non | Non | Non | ISO3 |
| fifa_code | VARCHAR(3) | Non | Non | Oui | Code FIFA |
| flag_url | TEXT | Non | Non | Oui | Drapeau |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_country_name
- idx_country_fifa

Contraintes UNIQUE:
- (iso2)
- (iso3)
- (fifa_code)

---

## cities

Description : Villes du monde entier.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| country_id | UUID | Non | countries.id | Non | Pays |
| name | VARCHAR(200) | Non | Non | Non | Nom de la ville |
| latitude | DECIMAL(9,6) | Non | Non | Oui | Latitude |
| longitude | DECIMAL(9,6) | Non | Non | Oui | Longitude |
| timezone | VARCHAR(100) | Non | Non | Oui | Fuseau horaire (ex: Europe/Paris) |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_city_country
- idx_city_name

---

# Module Confédérations

## confederations

Description : Confédérations continentales (UEFA, CAF, CONMEBOL, AFC, CONCACAF, OFC).

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| continent_id | UUID | Non | continents.id | Non | Continent |
| name | VARCHAR(200) | Non | Non | Non | Nom complet (ex: Union of European Football Associations) |
| acronym | VARCHAR(20) | Non | Non | Non | Acronyme (ex: UEFA) |
| logo | TEXT | Non | Non | Oui | Logo |
| website | TEXT | Non | Non | Oui | Site officiel |
| founded | INTEGER | Non | Non | Oui | Année de fondation |
| headquarters | VARCHAR(200) | Non | Non | Oui | Siège social |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_confederation_acronym

Contraintes UNIQUE:
- (acronym)

---

# Module Compétitions

## competitions

Description : Toutes les compétitions.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| country_id | UUID | Non | countries.id | Oui | Pays organisateur (NULL si international) |
| confederation_id | UUID | Non | confederations.id | Oui | Confédération |
| name | VARCHAR(200) | Non | Non | Non | Nom complet |
| short_name | VARCHAR(100) | Non | Non | Oui | Nom court |
| type | VARCHAR(50) | Non | Non | Non | Type : LEAGUE, LEAGUE_CUP, CUP, PLAYOFFS, SUPER_CUP, INTERNATIONAL, FRIENDLY |
| gender | VARCHAR(20) | Non | Non | Non | Genre : male, female, mixed |
| age_level | VARCHAR(20) | Non | Non | Non | Tranche d'âge : senior, U23, U20, U17 |
| sport | VARCHAR(50) | Non | Non | Non | Sport : football, futsal, beach_soccer |
| level | INTEGER | Non | Non | Oui | Niveau hiérarchique (1=première division) |
| logo | TEXT | Non | Non | Oui | Logo |
| founded | INTEGER | Non | Non | Oui | Année de création |
| website | TEXT | Non | Non | Oui | Site officiel |
| is_active | BOOLEAN | Non | Non | Non | Compétition active |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_competition_country
- idx_competition_type
- idx_competition_gender
- idx_competition_sport

Contraintes CHECK:
- gender IN ('male', 'female', 'mixed')
- type IN ('LEAGUE', 'LEAGUE_CUP', 'CUP', 'PLAYOFFS', 'SUPER_CUP', 'INTERNATIONAL', 'FRIENDLY')
- sport IN ('football', 'futsal', 'beach_soccer')

> **Source vérifiée** : football-data.org v4 utilise `LEAGUE | LEAGUE_CUP | CUP | PLAYOFFS`. Nous ajoutons `SUPER_CUP`, `INTERNATIONAL`, `FRIENDLY` pour compléter.

---

## seasons

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| competition_id | UUID | Non | competitions.id | Non | Compétition |
| name | VARCHAR(100) | Non | Non | Non | Ex : 2024/2025 |
| year_start | INTEGER | Non | Non | Non | Année de début |
| year_end | INTEGER | Non | Non | Non | Année de fin |
| start_date | DATE | Non | Non | Non | Date de début |
| end_date | DATE | Non | Non | Non | Date de fin |
| current | BOOLEAN | Non | Non | Non | Saison en cours |
| status | VARCHAR(20) | Non | Non | Non | UPCOMING, ONGOING, FINISHED |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_season_competition
- idx_season_current

Contraintes CHECK:
- status IN ('UPCOMING', 'ONGOING', 'FINISHED')

---

## stages

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| season_id | UUID | Non | seasons.id | Non | Saison |
| name | VARCHAR(100) | Non | Non | Non | Ex : Phase de groupes, Demi-finales |
| type | VARCHAR(50) | Non | Non | Non | GROUP, KNOCKOUT, LEAGUE |
| order_no | INTEGER | Non | Non | Non | Ordre d'affichage |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

---

## groups

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| stage_id | UUID | Non | stages.id | Non | Phase |
| name | VARCHAR(50) | Non | Non | Non | Ex : Groupe A |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

---

## rounds

Description : Journées ou tours d'une compétition (ex : Journée 12, Quart de finale, Leg 1).

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| stage_id | UUID | Non | stages.id | Non | Phase parente |
| group_id | UUID | Non | groups.id | Oui | Groupe lié (NULL si phase non groupée) |
| name | VARCHAR(100) | Non | Non | Non | Ex : Journée 12, Quart de finale |
| slug | VARCHAR(150) | Non | Non | Non | Ex : journee-12, quarter-final |
| number | INTEGER | Non | Non | Non | Numéro d'ordre dans la phase |
| type | VARCHAR(50) | Non | Non | Non | REGULAR, PLAYOFF, RELEGATION, FINAL |
| is_current | BOOLEAN | Non | Non | Non | Journée/Tour en cours |
| status | VARCHAR(20) | Non | Non | Non | UPCOMING, ONGOING, FINISHED |
| start_date | DATE | Non | Non | Oui | Date de début |
| end_date | DATE | Non | Non | Oui | Date de fin |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_round_stage
- idx_round_is_current
- idx_round_status

Contraintes UNIQUE:
- (stage_id, number)

Contraintes CHECK:
- type IN ('REGULAR', 'PLAYOFF', 'RELEGATION', 'FINAL')
- status IN ('UPCOMING', 'ONGOING', 'FINISHED')

---

## group_teams

Description : Participation des équipes dans un groupe (ex : PSG dans Groupe A de la Ligue des Champions 2024/25). Table pivot qui matérialise l'affectation de chaque équipe à un groupe spécifique dans une phase de compétition.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| group_id | UUID | Non | groups.id | Non | Groupe |
| team_id | UUID | Non | teams.id | Non | Équipe |
| seed | INTEGER | Non | Non | Oui | Tête de série (1, 2, 3...) |
| pot | INTEGER | Non | Non | Oui | Chapeau lors du tirage (1, 2, 3, 4) |
| qualified_from | VARCHAR(100) | Non | Non | Oui | Comment qualifié (ex: Champion national, Wild card) |
| eliminated | BOOLEAN | Non | Non | Non | Équipe éliminée du groupe |
| qualified | BOOLEAN | Non | Non | Non | Équipe qualifiée pour la phase suivante |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_group_teams_group
- idx_group_teams_team

Contraintes UNIQUE:
- (group_id, team_id)

---

## team_season_participations

Description : Inscription d'une équipe à une saison de compétition. Permet de savoir quelles équipes participent à quelle saison, avec des métadonnées de participation (promotion/relégation, résultat final).

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| season_id | UUID | Non | seasons.id | Non | Saison |
| team_id | UUID | Non | teams.id | Non | Équipe |
| entry_type | VARCHAR(50) | Non | Non | Non | PROMOTED, RELEGATED, CHAMPION, WILD_CARD, REGULAR |
| entry_from_competition_id | UUID | Non | competitions.id | Oui | Compétition depuis laquelle l'équipe arrive (relégation/promotion) |
| final_rank | INTEGER | Non | Non | Oui | Classement final en fin de saison |
| outcome | VARCHAR(50) | Non | Non | Oui | CHAMPION, PROMOTED, RELEGATED, PLAYOFF, QUALIFIED_UCL, QUALIFIED_UEL, QUALIFIED_UECL, REMAINED, WITHDRAWN |
| is_withdrawn | BOOLEAN | Non | Non | Non | Équipe retirée en cours de saison |
| withdrawal_date | DATE | Non | Non | Oui | Date de retrait si applicable |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_team_season_season
- idx_team_season_team

Contraintes UNIQUE:
- (season_id, team_id)

Contraintes CHECK:
- entry_type IN ('PROMOTED', 'RELEGATED', 'CHAMPION', 'WILD_CARD', 'REGULAR')
- outcome IN ('CHAMPION', 'PROMOTED', 'RELEGATED', 'PLAYOFF', 'QUALIFIED_UCL', 'QUALIFIED_UEL', 'QUALIFIED_UECL', 'REMAINED', 'WITHDRAWN')

---

# Module Équipes

## teams

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| country_id | UUID | Non | countries.id | Non | Pays |
| stadium_id | UUID | Non | stadiums.id | Oui | Stade principal |
| type | VARCHAR(50) | Non | Non | Non | Type d'équipe : MEN_CLUB, MEN_NATIONAL, WOMEN_CLUB, WOMEN_NATIONAL, MIXED_CLUB, REGIONAL |
| is_national_team | BOOLEAN | Non | Non | Non | Équipe nationale |
| name | VARCHAR(200) | Non | Non | Non | Nom complet |
| short_name | VARCHAR(100) | Non | Non | Oui | Nom court |
| code | VARCHAR(20) | Non | Non | Oui | Code (ex: PSG, FCB) |
| founded | INTEGER | Non | Non | Oui | Année de fondation |
| logo | TEXT | Non | Non | Oui | Logo |
| kit_primary_color | VARCHAR(7) | Non | Non | Oui | Couleur maillot domicile (hex) |
| kit_secondary_color | VARCHAR(7) | Non | Non | Oui | Couleur maillot extérieur (hex) |
| website | TEXT | Non | Non | Oui | Site officiel |
| address | TEXT | Non | Non | Oui | Adresse du club |
| phone | VARCHAR(50) | Non | Non | Oui | Téléphone |
| email | VARCHAR(200) | Non | Non | Oui | Email |
| description | TEXT | Non | Non | Oui | Description |
| is_active | BOOLEAN | Non | Non | Non | Club actif |
| deleted_at | TIMESTAMP | Non | Non | Oui | Suppression logique |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_team_country
- idx_team_name
- idx_team_type

Contraintes CHECK:
- type IN ('MEN_CLUB', 'MEN_NATIONAL', 'WOMEN_CLUB', 'WOMEN_NATIONAL', 'MIXED_CLUB', 'REGIONAL')

> **Source vérifiée** : football-data.org v4 utilise `MEN_CLUB | MEN_NATIONAL | WOMEN_CLUB | WOMEN_NATIONAL`. Le genre est désormais encodé directement dans le type.

---

## stadiums

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| country_id | UUID | Non | countries.id | Non | Pays |
| city_id | UUID | Non | cities.id | Non | Ville |
| name | VARCHAR(200) | Non | Non | Non | Nom du stade |
| capacity | INTEGER | Non | Non | Oui | Capacité totale |
| surface | VARCHAR(50) | Non | Non | Oui | grass, artificial, hybrid |
| latitude | DECIMAL(9,6) | Non | Non | Oui | Latitude |
| longitude | DECIMAL(9,6) | Non | Non | Oui | Longitude |
| address | TEXT | Non | Non | Oui | Adresse complète |
| opened | INTEGER | Non | Non | Oui | Année d'ouverture |
| image | TEXT | Non | Non | Oui | Photo du stade |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_stadium_city
- idx_stadium_country

Contraintes CHECK:
- surface IN ('grass', 'artificial', 'hybrid')

---

# Module Arbitres

## referees

Description : Arbitres principaux et assistants.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| country_id | UUID | Non | countries.id | Non | Nationalité |
| first_name | VARCHAR(100) | Non | Non | Oui | Prénom |
| last_name | VARCHAR(100) | Non | Non | Non | Nom |
| full_name | VARCHAR(200) | Non | Non | Non | Nom complet |
| birth_date | DATE | Non | Non | Oui | Date de naissance |
| photo | TEXT | Non | Non | Oui | Photo |
| category | VARCHAR(50) | Non | Non | Non | FIFA, NATIONAL, REGIONAL |
| is_active | BOOLEAN | Non | Non | Non | Arbitre actif |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_referee_country
- idx_referee_name

Contraintes CHECK:
- category IN ('FIFA', 'NATIONAL', 'REGIONAL')

---

## match_referees

Description : Arbitres assignés à un match (principal + assistants + VAR).

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| match_id | UUID | Non | matches.id | Non | Match |
| referee_id | UUID | Non | referees.id | Non | Arbitre |
| role | VARCHAR(50) | Non | Non | Non | Rôle de l'arbitre (REFEREE, ASSISTANT_REFEREE_N1/N2/N3, FOURTH_OFFICIAL, VIDEO_ASSISTANT_REFEREE_N1/N2/N3) |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |

Indexes:
- idx_match_referees_match
- idx_match_referees_referee

Contraintes CHECK:
- role IN ('REFEREE', 'ASSISTANT_REFEREE_N1', 'ASSISTANT_REFEREE_N2', 'ASSISTANT_REFEREE_N3', 'FOURTH_OFFICIAL', 'VIDEO_ASSISTANT_REFEREE_N1', 'VIDEO_ASSISTANT_REFEREE_N2', 'VIDEO_ASSISTANT_REFEREE_N3')

> **Source vérifiée** : Valeurs exactes issues de football-data.org v4 lookup tables.

Contraintes UNIQUE:
- (match_id, referee_id, role)

---

# Module Entraîneurs

## coaches

Description : Entraîneurs principaux et staff technique des équipes.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| country_id | UUID | Non | countries.id | Non | Nationalité |
| first_name | VARCHAR(100) | Non | Non | Oui | Prénom |
| last_name | VARCHAR(100) | Non | Non | Non | Nom |
| full_name | VARCHAR(200) | Non | Non | Non | Nom complet |
| birth_date | DATE | Non | Non | Oui | Date de naissance |
| photo | TEXT | Non | Non | Oui | Photo |
| role | VARCHAR(50) | Non | Non | Non | HEAD_COACH, ASSISTANT_COACH, GOALKEEPER_COACH, FITNESS_COACH |
| is_active | BOOLEAN | Non | Non | Non | Actif |
| deleted_at | TIMESTAMP | Non | Non | Oui | Suppression logique |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_coach_country
- idx_coach_name

Contraintes CHECK:
- role IN ('HEAD_COACH', 'ASSISTANT_COACH', 'GOALKEEPER_COACH', 'FITNESS_COACH')

---

## team_coaches

Description : Historique des entraîneurs par équipe et par saison. Un coach peut changer en cours de saison.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| team_id | UUID | Non | teams.id | Non | Équipe |
| coach_id | UUID | Non | coaches.id | Non | Entraîneur |
| season_id | UUID | Non | seasons.id | Non | Saison |
| role | VARCHAR(50) | Non | Non | Non | HEAD_COACH, ASSISTANT_COACH... |
| start_date | DATE | Non | Non | Non | Date de prise de fonction |
| end_date | DATE | Non | Non | Oui | Date de départ (NULL si en poste) |
| is_interim | BOOLEAN | Non | Non | Non | Entraîneur intérimaire |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_team_coaches_team
- idx_team_coaches_coach
- idx_team_coaches_season

Contraintes UNIQUE:
- (team_id, coach_id, season_id, role)

Contraintes CHECK:
- role IN ('HEAD_COACH', 'ASSISTANT_COACH', 'GOALKEEPER_COACH', 'FITNESS_COACH')

---

# Module Joueurs

## players

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| country_id | UUID | Non | countries.id | Non | Pays de naissance |
| nationality_id | UUID | Non | countries.id | Non | Nationalité principale |
| second_nationality_id | UUID | Non | countries.id | Oui | Seconde nationalité |
| first_name | VARCHAR(100) | Non | Non | Oui | Prénom |
| last_name | VARCHAR(100) | Non | Non | Non | Nom |
| full_name | VARCHAR(200) | Non | Non | Non | Nom complet |
| birth_date | DATE | Non | Non | Non | Date de naissance |
| birth_place | VARCHAR(100) | Non | Non | Oui | Lieu de naissance |
| height | DECIMAL(5,2) | Non | Non | Oui | Taille en cm |
| weight | DECIMAL(5,2) | Non | Non | Oui | Poids en kg |
| preferred_foot | VARCHAR(20) | Non | Non | Oui | left, right, both |
| position | VARCHAR(50) | Non | Non | Non | GK, CB, LB, RB, CM, CAM, CDM, LW, RW, ST |
| photo | TEXT | Non | Non | Oui | Photo |
| market_value | DECIMAL(15,2) | Non | Non | Oui | Valeur marchande en EUR |
| status | VARCHAR(50) | Non | Non | Non | ACTIVE, RETIRED, DECEASED |
| twitter | VARCHAR(200) | Non | Non | Oui | Lien Twitter/X |
| instagram | VARCHAR(200) | Non | Non | Oui | Lien Instagram |
| deleted_at | TIMESTAMP | Non | Non | Oui | Suppression logique |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_player_country
- idx_player_name
- idx_player_position
- idx_player_status

Contraintes CHECK:
- preferred_foot IN ('left', 'right', 'both')
- status IN ('ACTIVE', 'RETIRED', 'DECEASED')
- position IN ('GK', 'CB', 'LB', 'RB', 'CM', 'CAM', 'CDM', 'LW', 'RW', 'ST')

---

## player_season_registrations

Description : Enregistrement officiel d'un joueur pour une équipe lors d'une saison donnée. Correspond à la liste de joueurs déposée auprès de la compétition (squad registration). Différent du contrat de travail.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| player_id | UUID | Non | players.id | Non | Joueur |
| team_id | UUID | Non | teams.id | Non | Équipe |
| season_id | UUID | Non | seasons.id | Non | Saison |
| competition_id | UUID | Non | competitions.id | Non | Compétition concernée |
| shirt_number | INTEGER | Non | Non | Oui | Numéro de maillot pour cette saison |
| position | VARCHAR(50) | Non | Non | Oui | Position enregistrée : GK, CB, LB, RB, CM, CAM, CDM, LW, RW, ST |
| status | VARCHAR(50) | Non | Non | Non | REGISTERED, LOANED_OUT, INELIGIBLE, UNREGISTERED |
| registered_at | DATE | Non | Non | Oui | Date d'enregistrement officiel |
| unregistered_at | DATE | Non | Non | Oui | Date de désenregistrement (transfert hivernal, etc.) |
| is_captain | BOOLEAN | Non | Non | Non | Capitaine de l'équipe cette saison |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_player_season_reg_player
- idx_player_season_reg_team
- idx_player_season_reg_season

Contraintes UNIQUE:
- (player_id, team_id, season_id, competition_id)

Contraintes CHECK:
- status IN ('REGISTERED', 'LOANED_OUT', 'INELIGIBLE', 'UNREGISTERED')
- position IN ('GK', 'CB', 'LB', 'RB', 'CM', 'CAM', 'CDM', 'LW', 'RW', 'ST')

---

## player_season_stats

Description : Statistiques agrégées d'un joueur pour une saison entière dans une compétition. Vue consolidée calculée à partir de match_statistics_player.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| player_id | UUID | Non | players.id | Non | Joueur |
| team_id | UUID | Non | teams.id | Non | Équipe |
| season_id | UUID | Non | seasons.id | Non | Saison |
| competition_id | UUID | Non | competitions.id | Non | Compétition |
| appearances | INTEGER | Non | Non | Non | Nombre de matchs joués |
| appearances_as_starter | INTEGER | Non | Non | Non | Titularisations |
| minutes_played | INTEGER | Non | Non | Non | Minutes totales jouées |
| goals | INTEGER | Non | Non | Non | Buts marqués |
| assists | INTEGER | Non | Non | Non | Passes décisives |
| shots | INTEGER | Non | Non | Non | Tirs totaux |
| shots_on_target | INTEGER | Non | Non | Non | Tirs cadrés |
| xg | DECIMAL(8,3) | Non | Non | Oui | Expected Goals cumulés |
| key_passes | INTEGER | Non | Non | Non | Passes clés |
| passes | INTEGER | Non | Non | Non | Passes totales |
| passes_accurate | INTEGER | Non | Non | Non | Passes réussies |
| dribbles_attempted | INTEGER | Non | Non | Non | Dribbles tentés |
| dribbles_succeeded | INTEGER | Non | Non | Non | Dribbles réussis |
| tackles | INTEGER | Non | Non | Non | Tacles |
| interceptions | INTEGER | Non | Non | Non | Interceptions |
| fouls_committed | INTEGER | Non | Non | Non | Fautes commises |
| fouls_drawn | INTEGER | Non | Non | Non | Fautes subies |
| yellow_cards | INTEGER | Non | Non | Non | Cartons jaunes |
| red_cards | INTEGER | Non | Non | Non | Cartons rouges |
| saves | INTEGER | Non | Non | Non | Arrêts (gardien) |
| goals_conceded | INTEGER | Non | Non | Non | Buts encaissés (gardien) |
| clean_sheets | INTEGER | Non | Non | Non | Clean sheets (gardien) |
| avg_rating | DECIMAL(4,2) | Non | Non | Oui | Note moyenne sur 10 |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_player_season_stats_player
- idx_player_season_stats_season
- idx_player_season_stats_competition

Contraintes UNIQUE:
- (player_id, team_id, season_id, competition_id)

---

## contracts

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| player_id | UUID | Non | players.id | Non | Joueur |
| team_id | UUID | Non | teams.id | Non | Équipe |
| start_date | DATE | Non | Non | Non | Début du contrat |
| end_date | DATE | Non | Non | Non | Fin du contrat |
| salary | NUMERIC(15,2) | Non | Non | Oui | Salaire mensuel en EUR |
| shirt_number | INTEGER | Non | Non | Oui | Numéro de maillot (contrat de base) |
| is_current | BOOLEAN | Non | Non | Non | Contrat en cours |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_contract_player
- idx_contract_team

---

## transfers

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| player_id | UUID | Non | players.id | Non | Joueur |
| from_team_id | UUID | Non | teams.id | Oui | Club cédant |
| to_team_id | UUID | Non | teams.id | Non | Club acquéreur |
| transfer_date | DATE | Non | Non | Non | Date du transfert |
| fee | NUMERIC(15,2) | Non | Non | Oui | Montant en EUR |
| currency | CHAR(3) | Non | Non | Oui | Devise ISO (EUR, GBP...) |
| transfer_type | VARCHAR(50) | Non | Non | Non | PERMANENT, LOAN, FREE, RETURN_FROM_LOAN |
| season_id | UUID | Non | seasons.id | Non | Saison du transfert |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Contraintes CHECK:
- transfer_type IN ('PERMANENT', 'LOAN', 'FREE', 'RETURN_FROM_LOAN')

---

# Module Matchs

## matches

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| season_id | UUID | Non | seasons.id | Non | Saison |
| stage_id | UUID | Non | stages.id | Non | Phase |
| group_id | UUID | Non | groups.id | Oui | Groupe |
| round_id | UUID | Non | rounds.id | Non | Journée/Tour |
| home_team_id | UUID | Non | teams.id | Non | Équipe domicile |
| away_team_id | UUID | Non | teams.id | Non | Équipe extérieure |
| stadium_id | UUID | Non | stadiums.id | Oui | Stade |
| referee_id | UUID | Non | referees.id | Oui | Arbitre principal |
| kickoff | TIMESTAMP | Non | Non | Non | Date et heure de coup d'envoi (UTC) |
| status | VARCHAR(30) | Non | Non | Non | Statut du match (SCHEDULED, TIMED, IN_PLAY, PAUSED, EXTRA_TIME, PENALTY_SHOOTOUT, FINISHED, SUSPENDED, POSTPONED, CANCELLED, ABANDONED, AWARDED) |
| period | VARCHAR(20) | Non | Non | Oui | Période en cours : 1H, HT, 2H, ET1, ET_HT, ET2, PEN, FT |
| minute | INTEGER | Non | Non | Oui | Minute de jeu en cours |
| minute_extra | INTEGER | Non | Non | Oui | Minutes additionnelles |
| first_half_start | TIMESTAMP | Non | Non | Oui | Heure de début de la 1ère mi-temps |
| second_half_start | TIMESTAMP | Non | Non | Oui | Heure de début de la 2ème mi-temps |
| extra_time_start | TIMESTAMP | Non | Non | Oui | Heure de début des prolongations |
| penalty_shootout_start | TIMESTAMP | Non | Non | Oui | Heure de début des tirs au but |
| home_score | INTEGER | Non | Non | Oui | Score domicile (90 min) |
| away_score | INTEGER | Non | Non | Oui | Score extérieur (90 min) |
| home_ht_score | INTEGER | Non | Non | Oui | Score domicile à la mi-temps |
| away_ht_score | INTEGER | Non | Non | Oui | Score extérieur à la mi-temps |
| home_et_score | INTEGER | Non | Non | Oui | Score domicile après prolongations |
| away_et_score | INTEGER | Non | Non | Oui | Score extérieur après prolongations |
| home_penalty_score | INTEGER | Non | Non | Oui | Score aux tirs au but domicile |
| away_penalty_score | INTEGER | Non | Non | Oui | Score aux tirs au but extérieur |
| home_penalty_form | VARCHAR(30) | Non | Non | Oui | Séquence de réussite/échec des tirs au but de l'équipe domicile (ex: ooxox) |
| away_penalty_form | VARCHAR(30) | Non | Non | Oui | Séquence de réussite/échec des tirs au but de l'équipe extérieure (ex: oxoox) |
| attendance | INTEGER | Non | Non | Oui | Affluence |
| weather | VARCHAR(100) | Non | Non | Oui | Conditions météo |
| temperature | DECIMAL(4,1) | Non | Non | Oui | Température en °C |
| wind_speed | DECIMAL(5,1) | Non | Non | Oui | Vitesse du vent (km/h) |
| note | TEXT | Non | Non | Oui | Note complémentaire (ex: raison report) |
| deleted_at | TIMESTAMP | Non | Non | Oui | Suppression logique |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_match_kickoff
- idx_match_status
- idx_match_home
- idx_match_away
- idx_match_season
- idx_match_stage

Contraintes CHECK:
- status IN ('SCHEDULED', 'TIMED', 'IN_PLAY', 'PAUSED', 'EXTRA_TIME', 'PENALTY_SHOOTOUT', 'FINISHED', 'SUSPENDED', 'POSTPONED', 'CANCELLED', 'ABANDONED', 'AWARDED')
- period IN ('1H', 'HT', '2H', 'ET1', 'ET_HT', 'ET2', 'PEN', 'FT')

> **Source vérifiée** : Valeurs exactes issues de football-data.org v4. `TIMED` = programmé avec heure connue, `AWARDED` = match attribué sur tapis vert, `SUSPENDED` = interrompu temporairement.

---

## lineups

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| match_id | UUID | Non | matches.id | Non | Match |
| team_id | UUID | Non | teams.id | Non | Équipe |
| player_id | UUID | Non | players.id | Non | Joueur |
| starter | BOOLEAN | Non | Non | Non | Titulaire |
| captain | BOOLEAN | Non | Non | Non | Capitaine |
| shirt_number | INTEGER | Non | Non | Oui | Numéro de maillot pour ce match |
| position | VARCHAR(30) | Non | Non | Oui | Position jouée |
| position_x | DECIMAL(5,2) | Non | Non | Oui | Position X sur le terrain (0-100) |
| position_y | DECIMAL(5,2) | Non | Non | Oui | Position Y sur le terrain (0-100) |
| formation_slot | INTEGER | Non | Non | Oui | Slot dans la formation (1-11) |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Contraintes UNIQUE:
- (match_id, team_id, player_id)

---

## match_formations

Description : Formation tactique de chaque équipe pour un match.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| match_id | UUID | Non | matches.id | Non | Match |
| team_id | UUID | Non | teams.id | Non | Équipe |
| formation | VARCHAR(20) | Non | Non | Non | Ex : 4-3-3, 4-4-2, 3-5-2 |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |

Contraintes UNIQUE:
- (match_id, team_id)

---

## match_events

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| match_id | UUID | Non | matches.id | Non | Match |
| minute | INTEGER | Non | Non | Non | Minute de l'événement |
| extra_minute | INTEGER | Non | Non | Oui | Minute additionnelle |
| period | VARCHAR(20) | Non | Non | Non | Période : 1H, 2H, ET1, ET2, PEN |
| team_id | UUID | Non | teams.id | Non | Équipe concernée |
| player_id | UUID | Non | players.id | Oui | Joueur principal |
| related_player_id | UUID | Non | players.id | Oui | Joueur lié (ex: entrant lors d'une sub, passeur) |
| event_type | VARCHAR(50) | Non | Non | Non | Type d'événement (voir valeurs ci-dessous) |
| detail | VARCHAR(100) | Non | Non | Oui | Précision (ex: Left Foot, Header pour un but) |
| comments | TEXT | Non | Non | Oui | Commentaire libre |
| var_reviewed | BOOLEAN | Non | Non | Non | Événement revu par le VAR |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |

Valeurs possibles pour event_type :
- GOAL (type: REGULAR, OWN, PENALTY)
- PENALTY_MISS
- YELLOW_CARD
- YELLOW_RED (2e carton jaune = rouge indirect)
- RED_CARD (expulsion directe)
- SUBSTITUTION
- VAR_DECISION
- INJURY

Contraintes CHECK:
- event_type IN ('GOAL', 'PENALTY_MISS', 'YELLOW_CARD', 'YELLOW_RED', 'RED_CARD', 'SUBSTITUTION', 'VAR_DECISION', 'INJURY')
- period IN ('1H', 'HT', '2H', 'ET1', 'ET_HT', 'ET2', 'PEN', 'FT')

> **Source vérifiée** : football-data.org v4 utilise `REGULAR | OWN | PENALTY` pour les buts, `YELLOW | YELLOW_RED | RED` pour les cartons.

Indexes:
- idx_event_match
- idx_event_type
- idx_event_minute

## match_penalty_shootout_shots

Description : Détail de chaque tir lors d'une séance de tirs au but.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| match_id | UUID | Non | matches.id | Non | Match associé |
| team_id | UUID | Non | teams.id | Non | Équipe du tireur |
| player_id | UUID | Non | players.id | Non | Joueur tireur |
| goalkeeper_id | UUID | Non | players.id | Non | Gardien adverse |
| shot_order | INTEGER | Non | Non | Non | Ordre chronologique du tir dans la séance |
| round | INTEGER | Non | Non | Non | Numéro du round (1 à 5, puis mort subite) |
| status | VARCHAR(30) | Non | Non | Non | Résultat du tir (SCORED, MISSED, SAVED) |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |

Contraintes UNIQUE:
- (match_id, shot_order)

Contraintes CHECK:
- status IN ('SCORED', 'MISSED', 'SAVED')


## match_statistics_team

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| match_id | UUID | Non | matches.id | Non | Match |
| team_id | UUID | Non | teams.id | Non | Équipe |
| possession | DECIMAL(5,2) | Non | Non | Non | Possession (%) |
| shots | INTEGER | Non | Non | Non | Tirs totaux |
| shots_on_target | INTEGER | Non | Non | Non | Tirs cadrés |
| shots_off_target | INTEGER | Non | Non | Non | Tirs non cadrés |
| shots_blocked | INTEGER | Non | Non | Non | Tirs bloqués |
| corners | INTEGER | Non | Non | Non | Corners |
| free_kicks | INTEGER | Non | Non | Non | Coups francs |
| goal_kicks | INTEGER | Non | Non | Non | Dégagements aux 6 mètres |
| throw_ins | INTEGER | Non | Non | Non | Touches |
| offsides | INTEGER | Non | Non | Non | Hors-jeux |
| fouls | INTEGER | Non | Non | Non | Fautes commises |
| yellow_cards | INTEGER | Non | Non | Non | Cartons jaunes |
| yellow_red_cards | INTEGER | Non | Non | Non | Cartons jaunes-rouges (2e jaune) |
| red_cards | INTEGER | Non | Non | Non | Cartons rouges directs |
| passes | INTEGER | Non | Non | Non | Passes totales |
| passes_accurate | INTEGER | Non | Non | Non | Passes réussies |
| tackles | INTEGER | Non | Non | Non | Tacles |
| interceptions | INTEGER | Non | Non | Non | Interceptions |
| clearances | INTEGER | Non | Non | Non | Dégagements |
| saves | INTEGER | Non | Non | Non | Arrêts du gardien |
| xg | DECIMAL(6,3) | Non | Non | Oui | Expected Goals |
| xga | DECIMAL(6,3) | Non | Non | Oui | Expected Goals Against |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

> **Source vérifiée** : football-data.org v4 fournit `corner_kicks`, `free_kicks`, `goal_kicks`, `throw_ins`, `yellow_red_cards` — tous ajoutés.

Contraintes UNIQUE:
- (match_id, team_id)

---

## match_statistics_player

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| match_id | UUID | Non | matches.id | Non | Match |
| team_id | UUID | Non | teams.id | Non | Équipe |
| player_id | UUID | Non | players.id | Non | Joueur |
| minutes_played | INTEGER | Non | Non | Non | Minutes jouées |
| goals | INTEGER | Non | Non | Non | Buts |
| assists | INTEGER | Non | Non | Non | Passes décisives |
| shots | INTEGER | Non | Non | Non | Tirs |
| shots_on_target | INTEGER | Non | Non | Non | Tirs cadrés |
| xg | DECIMAL(6,3) | Non | Non | Oui | Expected Goals |
| key_passes | INTEGER | Non | Non | Non | Passes clés |
| passes | INTEGER | Non | Non | Non | Passes totales |
| passes_accurate | INTEGER | Non | Non | Non | Passes réussies |
| long_balls | INTEGER | Non | Non | Non | Longs ballons |
| crosses | INTEGER | Non | Non | Non | Centres |
| dribbles_attempted | INTEGER | Non | Non | Non | Dribbles tentés |
| dribbles_succeeded | INTEGER | Non | Non | Non | Dribbles réussis |
| tackles | INTEGER | Non | Non | Non | Tacles |
| interceptions | INTEGER | Non | Non | Non | Interceptions |
| clearances | INTEGER | Non | Non | Non | Dégagements |
| fouls_committed | INTEGER | Non | Non | Non | Fautes commises |
| fouls_drawn | INTEGER | Non | Non | Non | Fautes subies |
| yellow_cards | INTEGER | Non | Non | Non | Cartons jaunes |
| red_cards | INTEGER | Non | Non | Non | Cartons rouges |
| saves | INTEGER | Non | Non | Non | Arrêts (gardien) |
| goals_conceded | INTEGER | Non | Non | Non | Buts encaissés (gardien) |
| rating | DECIMAL(4,2) | Non | Non | Oui | Note sur 10 |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Contraintes UNIQUE:
- (match_id, player_id)

---

# Module Classements

## standings

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| season_id | UUID | Non | seasons.id | Non | Saison |
| stage_id | UUID | Non | stages.id | Non | Phase |
| group_id | UUID | Non | groups.id | Oui | Groupe |
| team_id | UUID | Non | teams.id | Non | Équipe |
| rank_position | INTEGER | Non | Non | Non | Classement |
| played | INTEGER | Non | Non | Non | Matchs joués |
| wins | INTEGER | Non | Non | Non | Victoires |
| draws | INTEGER | Non | Non | Non | Nuls |
| losses | INTEGER | Non | Non | Non | Défaites |
| home_wins | INTEGER | Non | Non | Non | Victoires à domicile |
| home_draws | INTEGER | Non | Non | Non | Nuls à domicile |
| home_losses | INTEGER | Non | Non | Non | Défaites à domicile |
| away_wins | INTEGER | Non | Non | Non | Victoires à l'extérieur |
| away_draws | INTEGER | Non | Non | Non | Nuls à l'extérieur |
| away_losses | INTEGER | Non | Non | Non | Défaites à l'extérieur |
| goals_for | INTEGER | Non | Non | Non | Buts marqués |
| goals_against | INTEGER | Non | Non | Non | Buts encaissés |
| goal_difference | INTEGER | Non | Non | Non | Différence de buts |
| points | INTEGER | Non | Non | Non | Points |
| form | VARCHAR(10) | Non | Non | Oui | Forme récente ex: WWDLW |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_standings_season
- idx_standings_group

Contraintes UNIQUE:
- (season_id, stage_id, group_id, team_id) (Note : requiert un index unique partiel si group_id est NULL ou UNIQUE NULLS NOT DISTINCT en PostgreSQL 15+)

---

# Module Blessures

## injuries

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| player_id | UUID | Non | players.id | Non | Joueur |
| team_id | UUID | Non | teams.id | Non | Équipe au moment de la blessure |
| match_id | UUID | Non | matches.id | Oui | Match lors duquel la blessure est survenue |
| injury_type | VARCHAR(100) | Non | Non | Non | Type de blessure (ex: déchirure musculaire) |
| body_part | VARCHAR(100) | Non | Non | Oui | Partie du corps concernée |
| severity | VARCHAR(50) | Non | Non | Non | MINOR, MODERATE, SEVERE |
| start_date | DATE | Non | Non | Non | Date de début |
| expected_return | DATE | Non | Non | Oui | Date de retour prévue |
| actual_return | DATE | Non | Non | Oui | Date de retour effective |
| status | VARCHAR(50) | Non | Non | Non | ACTIVE, RECOVERED |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_injury_player
- idx_injury_status

Contraintes CHECK:
- severity IN ('MINOR', 'MODERATE', 'SEVERE')
- status IN ('ACTIVE', 'RECOVERED')

---

# Module Suspensions

## suspensions

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| player_id | UUID | Non | players.id | Non | Joueur suspendu |
| team_id | UUID | Non | teams.id | Non | Équipe du joueur |
| competition_id | UUID | Non | competitions.id | Non | Compétition concernée |
| season_id | UUID | Non | seasons.id | Non | Saison concernée |
| card_type | VARCHAR(50) | Non | Non | Non | RED_CARD, SECOND_YELLOW, CUMULATIVE |
| reason | VARCHAR(255) | Non | Non | Oui | Motif de la suspension |
| start_date | DATE | Non | Non | Non | Date de début |
| end_date | DATE | Non | Non | Oui | Date de fin |
| matches_banned | INTEGER | Non | Non | Non | Nombre de matchs de suspension total |
| matches_remaining | INTEGER | Non | Non | Non | Matchs de suspension restants |
| status | VARCHAR(50) | Non | Non | Non | ACTIVE, SERVED |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_suspension_player
- idx_suspension_competition

Contraintes CHECK:
- card_type IN ('RED_CARD', 'SECOND_YELLOW', 'CUMULATIVE')
- status IN ('ACTIVE', 'SERVED')

---

# Module Palmarès

## trophies

Description : Référentiel des titres et récompenses.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| competition_id | UUID | Non | competitions.id | Oui | Compétition associée |
| name | VARCHAR(200) | Non | Non | Non | Nom du titre (ex: Ballon d'Or) |
| type | VARCHAR(50) | Non | Non | Non | TEAM, INDIVIDUAL |
| logo | TEXT | Non | Non | Oui | Logo/image |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

---

## team_trophies

Description : Titres remportés par les équipes.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| team_id | UUID | Non | teams.id | Non | Équipe |
| trophy_id | UUID | Non | trophies.id | Non | Titre |
| season_id | UUID | Non | seasons.id | Non | Saison |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |

Contraintes UNIQUE:
- (team_id, trophy_id, season_id)

---

## player_awards

Description : Récompenses individuelles des joueurs.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| player_id | UUID | Non | players.id | Non | Joueur |
| trophy_id | UUID | Non | trophies.id | Non | Récompense |
| season_id | UUID | Non | seasons.id | Non | Saison |
| team_id | UUID | Non | teams.id | Non | Équipe au moment de la récompense |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |

Contraintes UNIQUE:
- (player_id, trophy_id, season_id)

---

# Module Head-to-Head

## head_to_head

Description : Historique des confrontations entre deux équipes.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| team1_id | UUID | Non | teams.id | Non | Équipe 1 (ordre alphabétique) |
| team2_id | UUID | Non | teams.id | Non | Équipe 2 |
| total_matches | INTEGER | Non | Non | Non | Total de matchs joués |
| team1_wins | INTEGER | Non | Non | Non | Victoires équipe 1 |
| team2_wins | INTEGER | Non | Non | Non | Victoires équipe 2 |
| draws | INTEGER | Non | Non | Non | Nuls |
| team1_goals | INTEGER | Non | Non | Non | Buts marqués équipe 1 |
| team2_goals | INTEGER | Non | Non | Non | Buts marqués équipe 2 |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Contraintes UNIQUE:
- (team1_id, team2_id)

Contraintes CHECK:
- team1_id < team2_id (pour éviter les doublons symétriques)

---

# Module Paris Sportifs

## bookmakers

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| name | VARCHAR(100) | Non | Non | Non | Nom du bookmaker |
| website | TEXT | Non | Non | Oui | Site web |
| logo | TEXT | Non | Non | Oui | Logo |
| is_active | BOOLEAN | Non | Non | Non | Actif |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

---

## odds

Description : Cotes actuelles pour chaque marché.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| bookmaker_id | UUID | Non | bookmakers.id | Non | Bookmaker |
| match_id | UUID | Non | matches.id | Non | Match |
| market | VARCHAR(100) | Non | Non | Non | Marché (ex: 1X2, BTTS, Over2.5) |
| selection | VARCHAR(100) | Non | Non | Non | Sélection (ex: Home, Yes, Over) |
| odd | DECIMAL(8,3) | Non | Non | Non | Cote |
| is_active | BOOLEAN | Non | Non | Non | Cote toujours disponible |
| recorded_at | TIMESTAMP | Non | Non | Non | Horodatage de la cote |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_odds_match
- idx_odds_bookmaker

---

## odds_history

Description : Historique de l'évolution des cotes.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| bookmaker_id | UUID | Non | bookmakers.id | Non | Bookmaker |
| match_id | UUID | Non | matches.id | Non | Match |
| market | VARCHAR(100) | Non | Non | Non | Marché |
| selection | VARCHAR(100) | Non | Non | Non | Sélection |
| odd | DECIMAL(8,3) | Non | Non | Non | Cote à cet instant |
| recorded_at | TIMESTAMP | Non | Non | Non | Horodatage de la cote |

Indexes:
- idx_odds_history_match
- idx_odds_history_recorded_at

---

# Module Actualités

## news

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| competition_id | UUID | Non | competitions.id | Oui | Compétition liée |
| team_id | UUID | Non | teams.id | Oui | Équipe liée |
| player_id | UUID | Non | players.id | Oui | Joueur lié |
| title | VARCHAR(500) | Non | Non | Non | Titre |
| slug | VARCHAR(500) | Non | Non | Non | Slug URL SEO-friendly |
| content | TEXT | Non | Non | Non | Contenu |
| excerpt | TEXT | Non | Non | Oui | Résumé court |
| image | TEXT | Non | Non | Oui | Image principale |
| author | VARCHAR(200) | Non | Non | Oui | Auteur |
| language | VARCHAR(10) | Non | Non | Non | Code langue (fr, en, es...) |
| source_url | TEXT | Non | Non | Oui | URL source originale |
| published_at | TIMESTAMP | Non | Non | Non | Date de publication |
| deleted_at | TIMESTAMP | Non | Non | Oui | Suppression logique |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_news_published_at
- idx_news_language

Contraintes UNIQUE:
- (slug, language)

---

# Module Médias

## media

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| entity_type | VARCHAR(50) | Non | Non | Non | match, team, player, competition, stadium |
| entity_id | UUID | Non | Non | Non | Identifiant de l'entité cible |
| media_type | VARCHAR(50) | Non | Non | Non | PHOTO, VIDEO, HIGHLIGHT |
| title | VARCHAR(300) | Non | Non | Non | Titre du média |
| description | TEXT | Non | Non | Oui | Description |
| url | TEXT | Non | Non | Non | URL du média |
| thumbnail_url | TEXT | Non | Non | Oui | Vignette |
| duration | INTEGER | Non | Non | Oui | Durée en secondes (pour vidéos) |
| language | VARCHAR(10) | Non | Non | Oui | Langue du contenu |
| published_at | TIMESTAMP | Non | Non | Non | Date de publication |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Contraintes CHECK:
- media_type IN ('PHOTO', 'VIDEO', 'HIGHLIGHT')
- entity_type IN ('match', 'team', 'player', 'competition', 'stadium')

---

# Module Sponsors

## sponsors

Description : Entités sponsors (marques, entreprises partenaires).

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant (`idSponsor`) |
| nom | VARCHAR(200) | Non | Non | Non | Nom du sponsor |
| image_url | TEXT | Non | Non | Non | Logo / image du sponsor |
| description | TEXT | Non | Non | Oui | Description |
| website_url | TEXT | Non | Non | Oui | Site web officiel |
| rating | DECIMAL(3,1) | Non | Non | Oui | Score de priorité d'affichage |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_sponsor_nom

---

## sponsor_links

Description : Liaison polymorphique entre un sponsor et une entité (compétition, match, équipe, joueur, arbitre, coach).

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| sponsor_id | UUID | Non | sponsors.id | Non | Sponsor concerné |
| entity_type | VARCHAR(50) | Non | Non | Non | competition, match, team, player, referee, coach |
| entity_id | VARCHAR(100) | Non | Non | Non | Identifiant de l'entité cible |
| date_debut | DATE | Non | Non | Oui | Début du partenariat |
| date_fin | DATE | Non | Non | Oui | Fin du partenariat |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_sponsor_link_sponsor
- idx_sponsor_link_entity

Contraintes UNIQUE:
- (sponsor_id, entity_type, entity_id)

Contraintes CHECK:
- entity_type IN ('competition', 'match', 'team', 'player', 'referee', 'coach')

> **Note** : Correspond aux champs `idEdition`, `idGame`, `idParticipant`, `idJoueur`, `idArbitre`, `idCoach` de l'entité `Sponsor` Flutter.

---

# Module Traductions

## translations

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| entity_type | VARCHAR(50) | Non | Non | Non | Type de l'entité |
| entity_id | UUID | Non | Non | Non | Identifiant de l'entité |
| language | VARCHAR(10) | Non | Non | Non | Code langue (fr, en, es...) |
| field_name | VARCHAR(100) | Non | Non | Non | Champ traduit |
| translated_value | TEXT | Non | Non | Non | Valeur traduite |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Indexes:
- idx_translation_entity
- idx_translation_language

Contraintes UNIQUE:
- (entity_type, entity_id, language, field_name)

---

# Module API

## api_users

Description : Utilisateurs de l'API.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| email | VARCHAR(300) | Non | Non | Non | Email |
| name | VARCHAR(200) | Non | Non | Non | Nom |
| plan | VARCHAR(50) | Non | Non | Non | FREE, STARTER, PRO, ENTERPRISE |
| is_active | BOOLEAN | Non | Non | Non | Compte actif |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Contraintes UNIQUE:
- (email)

Contraintes CHECK:
- plan IN ('FREE', 'STARTER', 'PRO', 'ENTERPRISE')

---

## api_keys

Description : Clés d'API associées aux utilisateurs.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| user_id | UUID | Non | api_users.id | Non | Utilisateur |
| key_hash | VARCHAR(255) | Non | Non | Non | Hash SHA-256 de la clé |
| name | VARCHAR(200) | Non | Non | Non | Nom descriptif |
| is_active | BOOLEAN | Non | Non | Non | Clé active |
| expires_at | TIMESTAMP | Non | Non | Oui | Date d'expiration |
| last_used_at | TIMESTAMP | Non | Non | Oui | Dernière utilisation |
| created_at | TIMESTAMP | Non | Non | Non | Date de création |

Contraintes UNIQUE:
- (key_hash)

---

## rate_limits

Description : Quotas de requêtes par clé API.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| api_key_id | UUID | Non | api_keys.id | Non | Clé API |
| requests_per_minute | INTEGER | Non | Non | Non | Limite par minute |
| requests_per_day | INTEGER | Non | Non | Non | Limite par jour |
| requests_per_month | INTEGER | Non | Non | Non | Limite par mois |
| current_minute_count | INTEGER | Non | Non | Non | Compteur minute courant |
| current_day_count | INTEGER | Non | Non | Non | Compteur jour courant |
| current_month_count | INTEGER | Non | Non | Non | Compteur mois courant |
| updated_at | TIMESTAMP | Non | Non | Non | Date de mise à jour |

Contraintes UNIQUE:
- (api_key_id)

---

## audit_logs

Description : Journal d'accès et d'appels à l'API.

| Champ | Type | PK | FK | Nullable | Description |
|---------|---------|---------|---------|---------|---------|
| id | UUID | Oui | Non | Non | Identifiant |
| api_key_id | UUID | Non | api_keys.id | Non | Clé API utilisée |
| endpoint | VARCHAR(500) | Non | Non | Non | Endpoint appelé |
| method | VARCHAR(10) | Non | Non | Non | Méthode HTTP |
| status_code | INTEGER | Non | Non | Non | Code HTTP de réponse |
| response_time_ms | INTEGER | Non | Non | Non | Temps de réponse en ms |
| ip_address | INET | Non | Non | Non | Adresse IP du client |
| created_at | TIMESTAMP | Non | Non | Non | Date de l'appel |

Indexes:
- idx_audit_api_key
- idx_audit_created_at

---

# Recommandations PostgreSQL

## Extensions

- uuid-ossp (génération UUID)
- pg_trgm (recherche textuelle floue)
- postgis (géolocalisation pour stadiums/villes)
- timescaledb (si besoin de time-series pour odds_history)

## Indexes stratégiques

- matches(kickoff)
- matches(status)
- matches(home_team_id, away_team_id)
- teams(name)
- players(full_name)
- standings(season_id, team_id)
- match_events(match_id, minute)
- odds(match_id, bookmaker_id, market)
- odds_history(match_id, recorded_at)
- audit_logs(created_at)

## Bonnes pratiques

- Toutes les tables incluent `created_at` et `updated_at` (sauf tables de logs en insert-only)
- Suppression logique via `deleted_at` sur les entités principales (teams, players, matches, news)
- Toutes les clés primaires sont des UUID v4
- Les `TIMESTAMP` sont stockés en UTC
- Les montants financiers (fee, salary, market_value) sont en EUR par défaut, avec `currency` si besoin
- Les coordonnées géographiques utilisent `DECIMAL(9,6)` pour une précision de ~0.1 mm

---

# Module Stockage de fichiers (images / vidéos)

## Objectif

Stocker sur disque local les images et vidéos (logos, photos, images de news, drapeaux, médias) et les servir publiquement. Les entités conservent l'URL du fichier (`/uploads/...`) dans leurs champs existants (`logo`, `photo`, `image`, `flag_url`, `image_url`, `url`, `thumbnail_url`).

## Configuration

Fichier : `src/main/resources/application.properties`

```properties
# Dossier racine sur le disque (hors classpath). Les fichiers sont servis via /uploads/**
app.storage.location=./data/uploads
# Taille max par type (octets)
app.storage.max-image-size=10485760      # 10 Mo
app.storage.max-video-size=209715200     # 200 Mo

spring.servlet.multipart.max-file-size=210MB
spring.servlet.multipart.max-request-size=220MB
```

En prod, surcharger via env vars : `APP_STORAGE_LOCATION`, `APP_STORAGE_MAX_IMAGE_SIZE`, `APP_STORAGE_MAX_VIDEO_SIZE`.

## API

### Upload (authentifié, multipart)

`POST /api/files` — champ `file`

Réponse `201` :

```json
{
  "id": "uuid",
  "originalName": "logo.png",
  "fileName": "a1b2c3...png",
  "category": "images",
  "contentType": "image/png",
  "size": 12345,
  "url": "/uploads/images/a1b2c3...png",
  "createdAt": "2026-08-02T00:00:00"
}
```

### Liste des fichiers (authentifié)

`GET /api/files?page=0&size=20&category=images` — paginé, filtrable (voir FilterService).

### Détail (authentifié)

`GET /api/files/{id}`

### Suppression (authentifié)

`DELETE /api/files/{id}` — supprime la ligne + le fichier du disque.

### Lecture publique

`GET /uploads/**` — sert directement les fichiers depuis `app.storage.location` avec cache 30 jours et support des requêtes Range (streaming vidéo). Aucune authentification requise.

## Règles

- Types acceptés : `image/*` et `video/*` uniquement. Tout autre type => `400`.
- Taille limitée par catégorie : images 10 Mo, vidéos 200 Mo (configurable) => `413` au-delà.
- Nom de fichier généré en UUID (pas d'upload de chemin), extension conservée et assainie.
- Anti path traversal : tout chemin de fichier est normalisé et vérifié contre la racine de stockage.
- Nommage des dossiers par catégorie : `{location}/images/*`, `{location}/videos/*`.

## Table `stored_files`

| Champ | Type | Nullable | Description |
|-------|------|----------|-------------|
| id | UUID | Non | Identifiant |
| original_name | VARCHAR(255) | Non | Nom original du fichier |
| file_name | VARCHAR(255) | Non | Nom généré (UUID + extension) |
| category | VARCHAR(20) | Non | `images` ou `videos` |
| content_type | VARCHAR(100) | Non | Type MIME |
| size | BIGINT | Non | Taille en octets |
| url_path | VARCHAR(500) | Non | URL publique (`/uploads/...`) |
| created_at | TIMESTAMP | Non | Date d'upload |

## Backoffice admin

- Les champs de type `file` (`logo`, `photo`, `image`, `flagUrl`, `imageUrl`) dans `entity-config.ts` affichent un upload avec preview (image ou vidéo) et bouton Retirer. L'URL résultante est enregistrée dans le champ de l'entité.
- Le proxy de dev (`proxy.conf.json`) route `/api/` et `/uploads/` vers le backend (port 8080).
- Déploiement : après `npm run build` dans `football-admin/`, copier `dist/football-admin/*` vers `src/main/resources/static/`.

---

# Module Client Public (API `/api/client` + frontend `football-client/`)

## Objectif

Exposer des données **enrichies** (objets imbriqués : équipes, joueurs, compétitions, saisons) destinées à une
application grand public, sans que le client ait à faire des jointures. L'accès est sécurisé par **clé API**
(`X-Api-Key`), distincte de l'authentification admin (JWT). Les DTO sont en lecture seule.

## Authentification

- En-tête obligatoire : `X-Api-Key: fscore_...`
- Clés créées via le backoffice (`/api/api-keys`) et vérifiées par `ApiKeyAuthenticationFilter` +
  `ApiKeyService.authenticate()`.
- `/ws` (websocket STOMP) reste public (`permitAll`).
- Exemple :
  ```bash
  curl -H "X-Api-Key: fscore_xxx" "http://localhost:8080/api/client/matches?date=2026-08-02"
  ```

## Endpoints

| Méthode | URL | Paramètres | Description |
|---------|-----|------------|-------------|
| GET | `/api/client/matches` | `date` (ISO), `seasonId`, `competitionId`, `teamId`, `page`, `size` | Matchs paginés, triés par coup d'envoi |
| GET | `/api/client/matches/{id}` | — | Détail complet : scoreboard (MT/AP/PEN), événements, stats équipe/joueur, compositions |
| GET | `/api/client/competitions` | — | Compétitions actives avec saison courante |
| GET | `/api/client/competitions/{id}` | — | Compétition + liste des saisons |
| GET | `/api/client/seasons` | `competitionId` | Saisons d'une compétition |
| GET | `/api/client/standings` | `seasonId`, `stageId` (opt) | Classement d'une saison |
| GET | `/api/client/teams` | `seasonId` (opt) | Équipes d'une saison, ou toutes les équipes si omis |
| GET | `/api/client/teams/{id}` | — | Détail équipe : infos, pays, stade |
| GET | `/api/client/player-stats` | `seasonId`, `stat` (`scorers`/`assists`/`cards`) | Statistiques joueurs d'une saison |
| GET | `/api/client/news` | `competitionId` (opt), `teamId` (opt) | Actualités filtrées par compétition et/ou équipe |
| GET | `/api/client/referees` | `seasonId` | Arbitres d'une saison |
| GET | `/api/client/coaches` | `seasonId` | Coachs d'une saison |

## DTOs (`com.fscore.app.dto.client`)

Records sérialisés directement par Jackson (pas de Lombok, immuables) :

- `TeamRef` : id, name, shortName, code, logo, kitPrimaryColor, countryIso2, countryFlag
- `PlayerRef` : id, fullName, firstName, lastName, position, photo, preferredFoot
- `CompetitionRef` : id, name, shortName, type, gender, ageLevel, sport, logo, level, country/confederation, currentSeason
- `SeasonRef` : id, name, yearStart, yearEnd, startDate, endDate, current, status
- `MatchCard` : kickoff, status, period, minute, scores (FT/MT/AP/PEN), home/away `TeamRef`,
  `CompetitionRef`, `SeasonRef`, stage/group/round, stadium
- `MatchDetail` : `MatchCard` + referee, ville, affluence, météo, note, pénaltys, `events`, `teamStats`,
  `playerStats`, `lineups`
- `MatchEventItem`, `TeamStatItem`, `PlayerStatItem`, `LineupItem` (avec `team`), `StandingItem`, `CompetitionDetail`
- `PageInfo<T>` : structure paginée (compatible `content/totalPages/totalElements/size/number/first/last`)

## Implémentation

- `ClientApiService` : `@Transactional(readOnly = true)`, specifications dynamiques pour les filtres
  (date = intervalle de journée sur `kickoff`, `season.id`, `season.competition.id`, `homeTeam.id OR awayTeam.id`).
- `ClientController` : `@RequestMapping("/api/client")`.
- Repositories enrichis : `findByMatchIdOrderByMinuteAscExtraMinuteAsc` (events),
  `findByMatchIdOrderByStarterDescShirtNumberAsc` (lineups), `findByMatchId` (stats),
  `findBySeasonIdOrderByRankPositionAsc` (standings), `findByCompetitionIdOrderByYearStartDesc` (seasons).
- `MatchCard.competition` est résolu sans requête supplémentaire par saison (`includeSeason=false`).

## Frontend `football-client/`

Application Angular (22, standalone, Bootstrap 5.3 + bootstrap-icons) à **design moderne** :
navbar dégradé sombre, cartes arrondies avec effet de survol, badges de statut, dots "live" animés,
timeline d'événements, barres de comparaison de stats, tableau de classement coloré.

### Pages / routes

| Route | Contenu |
|-------|---------|
| `/` | Onglets **Matchs** (navigation par date, filtres compétition/saison/équipe, groupés par compétition, pagination), **Équipes** (grille cliquable), **Actualités** (news) |
| `/match/:id` | Scoreboard (avec MT/AP/PEN), onglets Résumé (timeline), Statistiques (comparaison + joueurs), Compositions (XI + remplaçants) |
| `/team/:id` | Détail équipe : infos (pays, stade), onglets Matchs (filtre date) et Actualités |
| `/competitions` | Grille de cartes compétitions (logo, pays/confédération, saison courante) |
| `/competition/:id` | Onglets Classement (avec zones promo/relégation + forme W/D/L), Matchs (filtre date), Équipes, Statistiques, Arbitres, News, Coachs |
| `/settings` | Clé API, URL websocket, test de connexion, statut du direct |

### Services

- `ApiService` : appels `/api/client/**` typés.
- `SettingsService` : clé API et URL broker persistées en `localStorage` ; clé par défaut préremplie.
- `LiveService` : client STOMP (`@stomp/stompjs`) sur `/ws`, reconnexion automatique, `subscribe(topic)` observable,
  état `connected()` (badge navbar).
- `api-key.interceptor` : ajoute `X-Api-Key` à toutes les requêtes `/api/`.

### Websocket (direct)

- Canaux écoutés : `/topic/live` (scores en direct), `/topic/events` (événements), `/topic/standings` (classement),
  `/topic/stats` (stats). Le détail de match met à jour score/minute/événements en place pour les matchs non terminés.

### Développement

```bash
# Backend
./mvnw spring-boot:run            # port 8080

# Frontend client
cd football-client
npm install
npm run start                     # port 4200, proxy /api, /uploads, /ws -> 8080
```

Le `proxy.conf.json` route `/api/`, `/uploads/` et `/ws` (websocket) vers le backend. La clé API par défaut est
préremplie dans `SettingsService` ; elle reste modifiable dans les paramètres de l'app.
