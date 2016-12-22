

describe("REST Service", function() {
    it("returns the user profile", function() {
    	let api = new RestApi(300, 400);
        expect(api.getProfile()).toContain("get Profile");
    });
});