<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

final class DeleteCityCommand
{
    /**
     * @var string
     */
    private $id;

    /**
     * DeleteCityCommand constructor.
     * @param $id
     */
    public function __construct(string $id)
    {
        $this->id = $id;
    }

    /**
     * @return mixed|string
     */
    public function getId() : string
    {
        return $this->id;
    }
}