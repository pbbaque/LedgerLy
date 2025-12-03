export interface ApiResponse<T> {
    message: string | null;
    data: T;
    token?: string;
    statusCode: number;
    success: boolean;   
}