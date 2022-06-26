export interface IRehber {
  id?: number;
  adi?: string;
  soyadi?: string;
  dahili?: string;
  cep?: string | null;
  aciklama?: string | null;
}

export const defaultValue: Readonly<IRehber> = {};
