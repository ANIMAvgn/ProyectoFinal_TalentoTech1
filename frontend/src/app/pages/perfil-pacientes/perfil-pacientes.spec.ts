import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilPacientes } from './perfil-pacientes';

describe('PerfilPacientes', () => {
  let component: PerfilPacientes;
  let fixture: ComponentFixture<PerfilPacientes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilPacientes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilPacientes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
