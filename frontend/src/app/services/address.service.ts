import { Injectable } from '@angular/core';
import { Address } from '../models/address';

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  static readonly COUNTRIES = [
    'España',
    'Estados Unidos',
    'México',
    'Argentina',
    'Colombia',
    'Chile',
    'Perú',
    'Venezuela',
    'Uruguay',
    'Paraguay',
    'Bolivia',
    'Ecuador',
    'Guatemala',
    'Costa Rica',
    'Panamá',
    'Honduras',
    'El Salvador',
    'Nicaragua',
    'Cuba',
    'República Dominicana',
    'Brasil',
    'Francia',
    'Alemania',
    'Italia',
    'Reino Unido',
    'Portugal',
    'Países Bajos',
    'Bélgica',
    'Suiza',
    'Austria',
    'Suecia',
    'Noruega',
    'Dinamarca',
    'Finlandia',
    'Rusia',
    'China',
    'Japón',
    'Corea del Sur',
    'India',
    'Australia',
    'Nueva Zelanda',
    'Canadá',
    'Sudáfrica',
    'Egipto',
    'Turquía',
    'Arabia Saudita',
    'Emiratos Árabes Unidos',
    'Israel',
    'Grecia',
  ];


  private phonePrefixes: Record<string, string> = {
    'España': '+34',
    'Estados Unidos': '+1',
    'México': '+52',
    'Argentina': '+54',
    'Colombia': '+57',
    'Chile': '+56',
    'Perú': '+51',
    'Venezuela': '+58',
    'Uruguay': '+598',
    'Paraguay': '+595',
    'Bolivia': '+591',
    'Ecuador': '+593',
    'Guatemala': '+502',
    'Costa Rica': '+506',
    'Panamá': '+507',
    'Honduras': '+504',
    'El Salvador': '+503',
    'Nicaragua': '+505',
    'Cuba': '+53',
    'República Dominicana': '+1-809',
    'Brasil': '+55',
    'Francia': '+33',
    'Alemania': '+49',
    'Italia': '+39',
    'Reino Unido': '+44',
    'Portugal': '+351',
    'Países Bajos': '+31',
    'Bélgica': '+32',
    'Suiza': '+41',
    'Austria': '+43',
    'Suecia': '+46',
    'Noruega': '+47',
    'Dinamarca': '+45',
    'Finlandia': '+358',
    'Rusia': '+7',
    'China': '+86',
    'Japón': '+81',
    'Corea del Sur': '+82',
    'India': '+91',
    'Australia': '+61',
    'Nueva Zelanda': '+64',
    'Canadá': '+1',
    'Sudáfrica': '+27',
    'Egipto': '+20',
    'Turquía': '+90',
    'Arabia Saudita': '+966',
    'Emiratos Árabes Unidos': '+971',
    'Israel': '+972',
    'Grecia': '+30'
  };

  constructor() { }

  getPhonePrefix(country: string): string {
    return this.phonePrefixes[country] || '';
  }

  formatPhone(country: string, phone: string): string {
    const prefix = this.getPhonePrefix(country);
    if (!prefix) {
      return phone;
    }
    return `${prefix} ${phone.trim()}`;
  }

  formatPhoneE164(country: string, phone: string): string {
    let prefix = this.getPhonePrefix(country);
    if (!prefix) return phone.replace(/\D/g, '');
    prefix = prefix.replace(/\D/g, ''); 
    return `+${prefix}${phone.replace(/\D/g, '')}`;
  }

  isValidPhone(phone: string): boolean {
    const digits = phone.replace(/\D/g, '');
    return digits.length >= 7 && digits.length <= 15;
  }

  isValidAddress(address: Address): boolean {
    return !!address.country && !!address.city && !!address.street && !!address.number;
  }

  formatAddress(address: Address): string {
    if (!this.isValidAddress(address)) {
      return '';
    }
    return `${address.street} ${address.number}, ${address.city}, ${address.country}`;
  }

  getCountries() {
    return AddressService.COUNTRIES;
  }

  filterCountries(searchTerm: string): string[] {
    const term = searchTerm.toLowerCase();
    return AddressService.COUNTRIES.filter(c => c.toLowerCase().includes(term));
  }
}
