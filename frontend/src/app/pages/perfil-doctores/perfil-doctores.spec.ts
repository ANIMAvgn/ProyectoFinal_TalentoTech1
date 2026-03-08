import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilDoctores } from './perfil-doctores';

describe('PerfilDoctores', () => {
  let component: PerfilDoctores;
  let fixture: ComponentFixture<PerfilDoctores>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilDoctores]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilDoctores);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
