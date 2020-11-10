package ca.ulaval.glo4002.reservation.api.report.presenter.material;

import ca.ulaval.glo4002.reservation.api.report.dto.MaterialReportDto;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;

import javax.ws.rs.core.Response;

public class MaterialReportPresenter {
    private final MaterialReportDtoFactory materialReportDtoFactory;

    public MaterialReportPresenter(MaterialReportDtoFactory materialReportDtoFactory) {
        this.materialReportDtoFactory = materialReportDtoFactory;
    }

    public Response presentReport(MaterialReport materialReport) {
        MaterialReportDto materialReportDto = materialReportDtoFactory.create(materialReport);
        return Response.ok().entity(materialReportDto).build();
    }
}
