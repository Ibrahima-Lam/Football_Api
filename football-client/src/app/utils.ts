export function isoDate(d: Date): string {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
}

export function todayIso(): string {
  return isoDate(new Date());
}

export function addDays(d: Date, days: number): Date {
  const copy = new Date(d);
  copy.setDate(copy.getDate() + days);
  return copy;
}

export function fmtTime(iso: string): string {
  const d = new Date(iso);
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
}

export function fmtDate(iso: string): string {
  return new Date(iso).toLocaleDateString('fr-FR', {
    weekday: 'short',
    day: 'numeric',
    month: 'short',
  });
}

export function fmtDateFull(iso: string): string {
  return new Date(iso).toLocaleDateString('fr-FR', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  });
}

export function isLive(m: { status: string }): boolean {
  return ['LIVE', 'IN_PLAY', '1ST_HALF', '2ND_HALF', 'HT', 'ET', 'PENALTY_SHOOTOUT'].includes(
    m.status?.toUpperCase(),
  );
}

export function isFinished(m: { status: string }): boolean {
  return ['FINISHED', 'FT', 'AET', 'PEN'].includes(m.status?.toUpperCase());
}

export function statusLabel(status: string): string {
  switch (status?.toUpperCase()) {
    case 'LIVE':
    case 'IN_PLAY':
    case '1ST_HALF':
    case '2ND_HALF':
    case 'HT':
      return 'En direct';
    case 'FINISHED':
    case 'FT':
      return 'Terminé';
    case 'AET':
      return 'Après prolongation';
    case 'PEN':
      return 'Aux tirs au but';
    case 'SCHEDULED':
      return 'Programmé';
    case 'POSTPONED':
      return 'Reporté';
    case 'CANCELLED':
      return 'Annulé';
    case 'SUSPENDED':
      return 'Suspendu';
    case 'TBD':
      return 'À définir';
    default:
      return status;
  }
}

export function displayScore(
  status: string,
  score: number | null,
  ht: number | null,
  et: number | null,
  pen: number | null,
): string {
  const upper = status?.toUpperCase();
  if (pen !== null && ['PEN', 'FINISHED', 'FT'].includes(upper) && score === null) {
    return String(pen);
  }
  return score !== null ? String(score) : '-';
}

export function competitionLabel(c: { name: string; shortName: string | null }): string {
  return c.shortName || c.name;
}

export function scoreSub(m: {
  homeHtScore: number | null;
  awayHtScore: number | null;
  homeEtScore: number | null;
  awayEtScore: number | null;
  homePenaltyScore: number | null;
  awayPenaltyScore: number | null;
}): string | null {
  const parts: string[] = [];
  if (m.homeHtScore !== null && m.awayHtScore !== null) parts.push(`MT ${m.homeHtScore}-${m.awayHtScore}`);
  if (m.homeEtScore !== null && m.awayEtScore !== null) parts.push(`AP ${m.homeEtScore}-${m.awayEtScore}`);
  if (m.homePenaltyScore !== null && m.awayPenaltyScore !== null) parts.push(`PÉN ${m.homePenaltyScore}-${m.awayPenaltyScore}`);
  return parts.length ? parts.join(' · ') : null;
}
