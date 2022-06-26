import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Rehber e2e test', () => {
  const rehberPageUrl = '/rehber';
  const rehberPageUrlPattern = new RegExp('/rehber(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rehberSample = { adi: 'Ball e-markets', soyadi: 'Soft architectures Jamaican', dahili: 'matrix' };

  let rehber: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rehbers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rehbers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rehbers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rehber) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rehbers/${rehber.id}`,
      }).then(() => {
        rehber = undefined;
      });
    }
  });

  it('Rehbers menu should load Rehbers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rehber');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Rehber').should('exist');
    cy.url().should('match', rehberPageUrlPattern);
  });

  describe('Rehber page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rehberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Rehber page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/rehber/new$'));
        cy.getEntityCreateUpdateHeading('Rehber');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rehberPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rehbers',
          body: rehberSample,
        }).then(({ body }) => {
          rehber = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rehbers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/rehbers?page=0&size=20>; rel="last",<http://localhost/api/rehbers?page=0&size=20>; rel="first"',
              },
              body: [rehber],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rehberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Rehber page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rehber');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rehberPageUrlPattern);
      });

      it('edit button click should load edit Rehber page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Rehber');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rehberPageUrlPattern);
      });

      it('last delete button click should delete instance of Rehber', () => {
        cy.intercept('GET', '/api/rehbers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('rehber').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rehberPageUrlPattern);

        rehber = undefined;
      });
    });
  });

  describe('new Rehber page', () => {
    beforeEach(() => {
      cy.visit(`${rehberPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Rehber');
    });

    it('should create an instance of Rehber', () => {
      cy.get(`[data-cy="adi"]`).type('Cambridgeshire digital purple').should('have.value', 'Cambridgeshire digital purple');

      cy.get(`[data-cy="soyadi"]`).type('Market Generic Research').should('have.value', 'Market Generic Research');

      cy.get(`[data-cy="dahili"]`).type('supply-chains Gardens Creative').should('have.value', 'supply-chains Gardens Creative');

      cy.get(`[data-cy="cep"]`).type('Shirt').should('have.value', 'Shirt');

      cy.get(`[data-cy="aciklama"]`).type('Toys Investment').should('have.value', 'Toys Investment');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rehber = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rehberPageUrlPattern);
    });
  });
});
