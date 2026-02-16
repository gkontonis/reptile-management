export interface Reptile {
  id: number;
  name: string;
  species: string;
  subspecies?: string;
  gender: 'MALE' | 'FEMALE' | 'UNKNOWN';
  birthDate?: string;
  acquisitionDate: string;
  enclosureId?: number;
  status: 'ACTIVE' | 'QUARANTINE' | 'DECEASED' | 'SOLD';
  notes?: string;
  highlightImageId?: number;
  createdAt: string;
  updatedAt: string;
}

export interface ReptileDetail extends Reptile {
  // Additional details for full reptile profile
  morph?: string;
  breeder?: string;
  purchasePrice?: number;
  weight?: number;
  lastWeightDate?: string;
  lastFedDate?: string;
  lastShedDate?: string;
  lastCleanedDate?: string;
}

export interface FeedingLog {
  id: number;
  reptileId: number;
  feedingDate: string;
  foodType: string;
  quantity: string;
  ate: boolean;
  notes?: string;
}

export interface WeightLog {
  id: number;
  reptileId: number;
  weightDate: string;
  weight: number;
  notes?: string;
}

export type ShedQuality = 'COMPLETE' | 'PARTIAL' | 'INCOMPLETE';

export interface SheddingLog {
  id: number;
  reptileId: number;
  sheddingDate: string;
  shedQuality: ShedQuality;
  ateShed: boolean;
  notes?: string;
}

export type PoopConsistency = 'NORMAL' | 'RUNNY' | 'HARD' | 'WATERY';

export interface PoopLog {
  id: number;
  reptileId: number;
  poopDate: string;
  consistency: PoopConsistency;
  color?: string;
  parasitesPresent: boolean;
  notes?: string;
}

export type CleaningType = 'SPOT_CLEAN' | 'FULL_CLEAN' | 'WATER_CHANGE' | 'DEEP_CLEAN';

export interface EnclosureCleaning {
  id: number;
  enclosureId: number;
  cleaningDate: string;
  cleaningType: CleaningType;
  substrateChanged: boolean;
  disinfected: boolean;
  notes?: string;
}

export interface Enclosure {
  id: number;
  name: string;
  type: 'TERRARIUM' | 'VIVARIUM' | 'AQUARIUM' | 'PALUDARIUM';
  dimensions: string; // e.g., "24x18x36 inches"
  substrate?: string;
  temperature?: number;
  humidity?: number;
  lighting?: string;
  notes?: string;
}

export interface ReptileImage {
  id: number;
  reptileId: number;
  filename: string;
  contentType: string;
  description?: string;
  size: number;
  uploadedAt: string;
}