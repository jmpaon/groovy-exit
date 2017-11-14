
def em = new ExitMatrix(6, 6, [[0,1,2,3,4,5], [-5,0,4,6,2,3], [5,-4,0,-2,1,0], [-1,-2,-3,0,-5,-5],[-1,-2,-3,0,0,-5],[-1,-2,-3,0,-5, 0]  ] )
em.show()


list = em.randomIndices(3)
println list

println em.impacts(list)

println em.relativeImpact(list)

println em.absMean()

def s = new ExitSampler(em, 10000)

println s.summedImpactMatrix()

// (0..1000).each { chain = em.chainIndices(1,2,5); print "$chain : "; print "impacts: ${em.impacts(chain)} : "; println em.relativeImpact(chain)}

