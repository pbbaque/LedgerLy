export interface JwtPayload {
    exp: number;
    sub: string;
    authorities: string[];
    companyId?: number;
}